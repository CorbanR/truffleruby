/*
 * Copyright (c) 2017, 2018, Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */

package org.truffleruby.launcher;

import org.graalvm.launcher.AbstractLanguageLauncher;
import org.graalvm.options.OptionCategory;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.truffleruby.launcher.options.CommandLineException;
import org.truffleruby.launcher.options.CommandLineOptions;
import org.truffleruby.launcher.options.CommandLineParser;
import org.truffleruby.launcher.options.ExecutionAction;
import org.truffleruby.launcher.options.OptionsCatalog;

import java.io.File;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RubyLauncher extends AbstractLanguageLauncher {

    public static final String LANGUAGE_ID = "ruby";
    public static final String ENGINE_ID = "truffleruby";
    public static final String LANGUAGE_VERSION = "2.3.5";
    public static final int LANGUAGE_REVISION = 59905;
    public static final String BOOT_SOURCE_NAME = "main_boot_source";
    public static final String RUBY_COPYRIGHT = "truffleruby - Copyright (c) 2013-2017 Oracle and/or its affiliates";

    // Properties set directly on the java command-line with -D for image building
    public static final String LIBSULONG_DIR = isAOT() ? System.getProperty("truffleruby.native.libsulong_dir") : null;
    public static final boolean PRE_INITIALIZE_CONTEXTS = System.getProperty("polyglot.engine.PreinitializeContexts") != null;

    // These system properties are used before outside the SDK option system
    public static boolean METRICS_TIME;
    public static final boolean METRICS_MEMORY_USED_ON_EXIT =
            Boolean.getBoolean("truffleruby.metrics.memory_used_on_exit");

    private final CommandLineOptions config = new CommandLineOptions();

    public static void main(String[] args) {
        new RubyLauncher().launch(args);
    }

    // TODO (pitr-ch 22-Feb-2018): replace with a call to sdk API when available
    static boolean isGraal() {
        final CommandLineOptions config = new CommandLineOptions();
        config.setOption(OptionsCatalog.GRAAL_WARNING_UNLESS, false);
        config.setOption(OptionsCatalog.POST_BOOT, false);
        config.setOption(OptionsCatalog.NO_HOME_PROVIDED, true);

        try (Context context = RubyLauncher.createContext(Context.newBuilder(), config)) {
            final Source source = Source.newBuilder(
                    LANGUAGE_ID,
                    // language=ruby
                    "Truffle.graal?",
                    BOOT_SOURCE_NAME).internal(true).buildLiteral();
            return context.eval(source).asBoolean();
        } catch (PolyglotException e) {
            System.err.println("truffleruby: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static String getVersionString(boolean isGraal) {
        return String.format(
                "%s %s, like ruby %s <%s %s %s> [%s-%s]",
                ENGINE_ID,
                getEngineVersion(),
                LANGUAGE_VERSION,
                isAOT() ? "native" : System.getProperty("java.vm.name", "unknown JVM"),
                isAOT() ? "build" : System.getProperty(
                        "java.runtime.version",
                        System.getProperty("java.version", "unknown runtime version")),
                isGraal ? "with Graal" : "without Graal",
                BasicPlatform.getArchitecture(),
                BasicPlatform.getOSName()
        );
    }

    public static String getEngineVersion() {
        // The property cannot be read in a static initializer, it's set later
        final String systemVersion = System.getProperty("org.graalvm.version");

        // No version information, or just "dev" - use 0.0-commit
        if (systemVersion == null || systemVersion.equals("dev")) {
            return "0.0-" + BuildInformationImpl.INSTANCE.getRevision();
        }

        // A "-dev" version number - append the commit as well
        if (systemVersion.endsWith("-dev")) {
            return systemVersion + "-" + BuildInformationImpl.INSTANCE.getRevision();
        }


        return systemVersion;
    }

    // TODO (pitr-ch 23-Feb-2018): extract to a class
    public static void printTruffleTimeMetric(String id) {
        if (METRICS_TIME) {
            final long millis = System.currentTimeMillis();
            System.err.printf("%s %d.%03d%n", id, millis / 1000, millis % 1000);
        }
    }

    @Override
    protected String getLanguageId() {
        return LANGUAGE_ID;
    }

    @Override
    protected String getMainClass() {
        return RubyLauncher.class.getName();
    }

    @Override
    protected void validateArguments(Map<String, String> polyglotOptions) {
    }

    @Override
    protected void printVersion() {
        System.out.println(getVersionString(isGraal()));
        System.out.println();
        printPolyglotVersions();
    }

    @Override
    protected List<String> preprocessArguments(List<String> args, Map<String, String> polyglotOptions) {
        metricsBegin();

        try {
            config.setOption(OptionsCatalog.EXECUTION_ACTION, ExecutionAction.UNSET);

            final CommandLineParser argumentCommandLineParser = new CommandLineParser(args, config, true, false);
            argumentCommandLineParser.processArguments();

            if (config.getOption(OptionsCatalog.READ_RUBYOPT)) {
                final List<String> rubyoptArgs = getArgsFromEnvVariable("RUBYOPT");
                final List<String> trufflerubyoptArgs = getArgsFromEnvVariable("TRUFFLERUBYOPT");
                new CommandLineParser(rubyoptArgs, config, false, true).processArguments();
                new CommandLineParser(trufflerubyoptArgs, config, false, false).processArguments();

                if (isAOT()) {
                    // Append options from ENV variables to args after last interpreter option, which makes sure that
                    // maybeExec processes --(native|jvm)* options. The options are removed and are not passed to the
                    // new process if exec is being called.
                    // The new process gets all arguments and options including those from ENV variables.
                    // To avoid processing options from ENV variables twice READ_RUBYOPT option is set to false.
                    // Only native launcher can apply native and jvm options, therefore this is not done on JVM.
                    final int index = argumentCommandLineParser.getLastInterpreterArgumentIndex();
                    args.add(index, "-Xread_rubyopt=false");
                    args.addAll(index + 1, rubyoptArgs);
                    args.addAll(index + 1 + rubyoptArgs.size(), trufflerubyoptArgs);
                }
            }

            if (isAOT()) {
                // if applied store the options in polyglotOptions otherwise it would be lost when
                // switched to --jvm
                if (config.getOption(OptionsCatalog.HOME).isEmpty()) {
                    final String rubyHome = getGraalVMHome().resolve(Paths.get("jre", "languages", "ruby")).toString();
                    config.setOption(OptionsCatalog.HOME, rubyHome);
                    polyglotOptions.put(OptionsCatalog.HOME.getName(), rubyHome);
                }
                final String launcher = setRubyLauncherIfNative();
                if (launcher != null) {
                    polyglotOptions.put(OptionsCatalog.LAUNCHER.getName(), launcher);
                }
            }

            if (config.getOption(OptionsCatalog.EXECUTION_ACTION) == ExecutionAction.UNSET) {
                config.getOption(OptionsCatalog.DEFAULT_EXECUTION_ACTION).applyTo(config);
            }
        } catch (CommandLineException commandLineException) {
            System.err.println("truffleruby: " + commandLineException.getMessage());
            if (commandLineException.isUsageError()) {
                printHelp(System.err);
            }
            System.exit(1);
        }

        return config.getUnknownArguments();
    }

    @Override
    protected void launch(Context.Builder contextBuilder) {
        printPreRunInformation(config);
        debugPreInitialization();
        final int exitValue = runRubyMain(contextBuilder, config);
        metricsEnd();
        System.exit(exitValue);
    }

    @Override
    protected void collectArguments(Set<String> options) {
        options.addAll(Arrays.asList(
                "-0",
                "-a",
                "-c",
                "-C",
                "-d", "--debug",
                "-e",
                "-E", "--encoding",
                "-F",
                "-i",
                "-I",
                "-l",
                "-n",
                "-p",
                "-r",
                "-s",
                "-S",
                "-T",
                "-v", "--verbose",
                "-w",
                "-W",
                "-x",
                "--copyright",
                "--enable", "--disable",
                "--external-encoding", "--internal-encoding",
                "--version",
                "--help",
                "-Xlog",
                "-Xoptions"));
    }

    @Override
    protected VMType getDefaultVMType() {
        return VMType.JVM;
    }

    @Override
    protected String[] getDefaultLanguages() {
        return new String[]{ getLanguageId(), "llvm" };
    }

    @Override
    protected void printHelp(OptionCategory maxCategory) {
        printHelp(System.out);
    }

    private static int runRubyMain(Context.Builder contextBuilder, CommandLineOptions config) {
        if (config.getOption(OptionsCatalog.EXECUTION_ACTION) == ExecutionAction.NONE) {
            return 0;
        }

        try (Context context = createContext(contextBuilder, config)) {
            printTruffleTimeMetric("before-run");
            final Source source = Source.newBuilder(
                    LANGUAGE_ID,
                    // language=ruby
                    "Truffle::Boot.main",
                    BOOT_SOURCE_NAME).internal(true).buildLiteral();
            final int exitCode = context.eval(source).asInt();
            printTruffleTimeMetric("after-run");
            return exitCode;
        } catch (PolyglotException e) {
            System.err.println("truffleruby: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private static void debugPreInitialization() {
        if (!isAOT() && PRE_INITIALIZE_CONTEXTS) {
            try {
                final Class<?> holderClz = Class.forName("org.graalvm.polyglot.Engine$ImplHolder");
                final Method preInitMethod = holderClz.getDeclaredMethod("preInitializeEngine");
                preInitMethod.setAccessible(true);
                preInitMethod.invoke(null);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printTruffleMemoryMetric() {
        // Memory stats aren't available in native.
        if (!isAOT() && METRICS_MEMORY_USED_ON_EXIT) {
            for (int n = 0; n < 10; n++) {
                System.gc();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.err.printf("allocated %d%n", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed());
        }
    }

    private static Context createContext(Context.Builder builder, CommandLineOptions config) {
        builder.allowCreateThread(true);

        /*
         * We turn off using the polyglot IO streams when running from our launcher, because they don't act like
         * normal file descriptors and this can cause problems in some advanced IO functionality, such as pipes and
         * blocking behaviour. We also turn off sync on stdio and so revert to Ruby's default logic for looking
         * at whether a file descriptor looks like a TTY for deciding whether to make it synchronous or not.
         */
        builder.option(OptionsCatalog.POLYGLOT_STDIO.getName(), Boolean.FALSE.toString());
        builder.option(OptionsCatalog.SYNC_STDIO.getName(), Boolean.FALSE.toString());

        // When building a native image outside of GraalVM, we need to give the path to libsulong
        if (LIBSULONG_DIR != null) {
            final String launcher = config.getOption(OptionsCatalog.LAUNCHER);
            final String rubyHome = new File(launcher).getParentFile().getParent();
            final String libSulongPath = rubyHome + File.separator + LIBSULONG_DIR;

            String libraryPath = System.getProperty("polyglot.llvm.libraryPath");
            if (libraryPath == null || libraryPath.isEmpty()) {
                libraryPath = libSulongPath;
            } else {
                libraryPath = libraryPath + ":" + libSulongPath;
            }
            builder.option("llvm.libraryPath", libraryPath);
        }

        builder.options(config.getOptions());
        builder.arguments(LANGUAGE_ID, config.getArguments());

        return builder.build();
    }

    private static List<String> getArgsFromEnvVariable(String name) {
        String value = System.getenv(name);
        if (value != null) {
            value = value.trim();
            if (value.length() != 0) {
                return new ArrayList<>(Arrays.asList(value.split("\\s+")));
            }
        }
        return Collections.emptyList();
    }

    private static void metricsEnd() {
        printTruffleTimeMetric("after-main");
        printTruffleMemoryMetric();
    }

    private static void metricsBegin() {
        // Assigned here so it's available on SVM as well
        METRICS_TIME = Boolean.getBoolean("truffleruby.metrics.time");

        printTruffleTimeMetric("before-main");
    }

    private String setRubyLauncherIfNative() {
        if (isAOT() && config.getOption(OptionsCatalog.LAUNCHER).isEmpty()) {
            final String launcher = (String) Compiler.
                    command(new Object[]{ "com.oracle.svm.core.posix.GetExecutableName" });
            config.setOption(OptionsCatalog.LAUNCHER, launcher);
            return launcher;
        }
        return null;
    }


    private static Path getGraalVMHome() {
        final String graalVMHome = System.getProperty("org.graalvm.home");
        assert graalVMHome != null;
        return Paths.get(graalVMHome);
    }

    private static void printPreRunInformation(CommandLineOptions config) {
        if (config.isIrbInsteadOfInputUsed()) {
            RubyLogger.LOGGER.warning(
                    "by default truffleruby drops into IRB instead of reading stdin as MRI - " +
                            "use '-' to explicitly read from stdin");
        }

        if (config.getOption(OptionsCatalog.SHOW_VERSION)) {
            System.out.println(getVersionString(isGraal()));
        }

        if (config.getOption(OptionsCatalog.SHOW_COPYRIGHT)) {
            System.out.println(RUBY_COPYRIGHT);
        }

        switch (config.getOption(OptionsCatalog.SHOW_HELP)) {
            case NONE:
                break;
            case SHORT:
                printShortHelp(System.out);
                break;
            case LONG:
                printHelp(System.out);
                break;
        }
    }

    private static void printHelp(PrintStream out) {
        out.printf("Usage: %s [switches] [--] [programfile] [arguments]%n", ENGINE_ID);
        out.println("  -0[octal]       specify record separator (\0, if no argument)");
        out.println("  -a              autosplit mode with -n or -p (splits $_ into $F)");
        out.println("  -c              check syntax only");
        out.println("  -Cdirectory     cd to directory before executing your script");
        out.println("  -d, --debug     set debugging flags (set $DEBUG to true)");
        out.println("  -e 'command'    one line of script. Several -e's allowed. Omit [programfile]");
        out.println("  -Eex[:in], --encoding=ex[:in]");
        out.println("                  specify the default external and internal character encodings");
        out.println("  -Fpattern       split() pattern for autosplit (-a)");
        out.println("  -i[extension]   edit ARGV files in place (make backup if extension supplied)");
        out.println("  -Idirectory     specify $LOAD_PATH directory (may be used more than once)");
        out.println("  -l              enable line ending processing");
        out.println("  -n              assume 'while gets(); ... end' loop around your script");
        out.println("  -p              assume loop like -n but print line also like sed");
        out.println("  -rlibrary       require the library before executing your script");
        out.println("  -s              enable some switch parsing for switches after script name");
        out.println("  -S              look for the script using PATH environment variable");
        out.println("  -T[level=1]     turn on tainting checks");
        out.println("  -v, --verbose   print version number, then turn on verbose mode");
        out.println("  -w              turn warnings on for your script");
        out.println("  -W[level=2]     set warning level; 0=silence, 1=medium, 2=verbose");
        out.println("  -x[directory]   strip off text before #!ruby line and perhaps cd to directory");
        out.println("  --copyright     print the copyright");
        out.println("  --enable=feature[,...], --disable=feature[,...]");
        out.println("                  enable or disable features");
        out.println("  --external-encoding=encoding, --internal-encoding=encoding");
        out.println("                  specify the default external or internal character encoding");
        out.println("  --version       print the version");
        out.println("  --help          show this message, -h for short message");
        out.println("Features:");
        out.println("  gems            rubygems (default: enabled)");
        out.println("  did_you_mean    did_you_mean (default: enabled)");
        out.println("  rubyopt         RUBYOPT environment variable (default: enabled)");
        out.println("  frozen-string-literal");
        out.println("                  freeze all string literals (default: disabled)");
        out.println("TruffleRuby switches:");
        out.println("  -Xlog=severe,warning,performance,info,config,fine,finer,finest");
        out.println("                  set the TruffleRuby logging level");
        out.println("  -Xoptions       print available TruffleRuby options");
        out.println("  -Xname=value    set a TruffleRuby option (omit value to set to true)");

        if (isAOT()) {
            out.println("Native switches:");
            out.println("  -XX:arg         pass arg to the SVM");
            out.println("  -Dname=value    set a system property");
        } else {
            out.println("JVM switches:");
            out.println("  -J-arg, -J:arg, --jvm.arg      pass arg to the JVM");
        }
    }

    private static void printShortHelp(PrintStream out) {
        out.println("Usage: truffleruby [switches] [--] [programfile] [arguments]");
        out.println("  -0[octal]       specify record separator (\0, if no argument)");
        out.println("  -a              autosplit mode with -n or -p (splits $_ into $F)");
        out.println("  -c              check syntax only");
        out.println("  -Cdirectory     cd to directory before executing your script");
        out.println("  -d              set debugging flags (set $DEBUG to true)");
        out.println("  -e 'command'    one line of script. Several -e's allowed. Omit [programfile]");
        out.println("  -Eex[:in]       specify the default external and internal character encodings");
        out.println("  -Fpattern       split() pattern for autosplit (-a)");
        out.println("  -i[extension]   edit ARGV files in place (make backup if extension supplied)");
        out.println("  -Idirectory     specify $LOAD_PATH directory (may be used more than once)");
        out.println("  -l              enable line ending processing");
        out.println("  -n              assume 'while gets(); ... end' loop around your script");
        out.println("  -p              assume loop like -n but print line also like sed");
        out.println("  -rlibrary       require the library before executing your script");
        out.println("  -s              enable some switch parsing for switches after script name");
        out.println("  -S              look for the script using PATH environment variable");
        out.println("  -T[level=1]     turn on tainting checks");
        out.println("  -v              print version number, then turn on verbose mode");
        out.println("  -w              turn warnings on for your script");
        out.println("  -W[level=2]     set warning level; 0=silence, 1=medium, 2=verbose");
        out.println("  -x[directory]   strip off text before #!ruby line and perhaps cd to directory");
        out.println("  -h              show this message, --help for more info");
    }

}
