// created by jay 1.0.2 (c) 2002-2004 ats@cs.rit.edu
// skeleton Java 1.0 (c) 2002 ats@cs.rit.edu

					// line 2 "RubyParser.y"
/***** BEGIN LICENSE BLOCK *****
 * Version: EPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2008-2009 Thomas E Enebo <enebo@acm.org>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.parser;

import java.io.IOException;

import org.jruby.ast.ArgsNode;
import org.jruby.ast.ArgumentNode;
import org.jruby.ast.ArrayNode;
import org.jruby.ast.AssignableNode;
import org.jruby.ast.BackRefNode;
import org.jruby.ast.BeginNode;
import org.jruby.ast.BlockAcceptingNode;
import org.jruby.ast.BlockArgNode;
import org.jruby.ast.BlockNode;
import org.jruby.ast.BlockPassNode;
import org.jruby.ast.BreakNode;
import org.jruby.ast.CallNode;
import org.jruby.ast.ClassNode;
import org.jruby.ast.ClassVarNode;
import org.jruby.ast.ClassVarAsgnNode;
import org.jruby.ast.Colon3Node;
import org.jruby.ast.ConstNode;
import org.jruby.ast.ConstDeclNode;
import org.jruby.ast.DStrNode;
import org.jruby.ast.DSymbolNode;
import org.jruby.ast.DXStrNode;
import org.jruby.ast.DefnNode;
import org.jruby.ast.DefsNode;
import org.jruby.ast.DotNode;
import org.jruby.ast.EncodingNode;
import org.jruby.ast.EnsureNode;
import org.jruby.ast.EvStrNode;
import org.jruby.ast.FalseNode;
import org.jruby.ast.FileNode;
import org.jruby.ast.FCallNode;
import org.jruby.ast.FixnumNode;
import org.jruby.ast.FloatNode;
import org.jruby.ast.ForNode;
import org.jruby.ast.GlobalAsgnNode;
import org.jruby.ast.GlobalVarNode;
import org.jruby.ast.HashNode;
import org.jruby.ast.IfNode;
import org.jruby.ast.InstAsgnNode;
import org.jruby.ast.InstVarNode;
import org.jruby.ast.IterNode;
import org.jruby.ast.LambdaNode;
import org.jruby.ast.ListNode;
import org.jruby.ast.LiteralNode;
import org.jruby.ast.ModuleNode;
import org.jruby.ast.MultipleAsgn19Node;
import org.jruby.ast.NextNode;
import org.jruby.ast.NilImplicitNode;
import org.jruby.ast.NilNode;
import org.jruby.ast.Node;
import org.jruby.ast.NonLocalControlFlowNode;
import org.jruby.ast.OpAsgnAndNode;
import org.jruby.ast.OpAsgnNode;
import org.jruby.ast.OpAsgnOrNode;
import org.jruby.ast.OptArgNode;
import org.jruby.ast.PostExeNode;
import org.jruby.ast.PreExe19Node;
import org.jruby.ast.RationalNode;
import org.jruby.ast.RedoNode;
import org.jruby.ast.RegexpNode;
import org.jruby.ast.RequiredKeywordArgumentValueNode;
import org.jruby.ast.RescueBodyNode;
import org.jruby.ast.RescueNode;
import org.jruby.ast.RestArgNode;
import org.jruby.ast.RetryNode;
import org.jruby.ast.ReturnNode;
import org.jruby.ast.SClassNode;
import org.jruby.ast.SelfNode;
import org.jruby.ast.StarNode;
import org.jruby.ast.StrNode;
import org.jruby.ast.SymbolNode;
import org.jruby.ast.TrueNode;
import org.jruby.ast.UnnamedRestArgNode;
import org.jruby.ast.UntilNode;
import org.jruby.ast.VAliasNode;
import org.jruby.ast.WhileNode;
import org.jruby.ast.XStrNode;
import org.jruby.ast.YieldNode;
import org.jruby.ast.ZArrayNode;
import org.jruby.ast.ZSuperNode;
import org.jruby.ast.ZYieldNode;
import org.jruby.ast.types.ILiteralNode;
import org.jruby.common.IRubyWarnings;
import org.jruby.common.IRubyWarnings.ID;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.lexer.yacc.ISourcePositionHolder;
import org.jruby.lexer.yacc.LexerSource;
import org.jruby.lexer.yacc.RubyLexer;
import org.jruby.lexer.yacc.RubyLexer.LexState;
import org.jruby.lexer.yacc.StrTerm;
import org.jruby.lexer.yacc.SyntaxException;
import org.jruby.lexer.yacc.SyntaxException.PID;
import org.jruby.util.ByteList;
import org.jruby.util.KeyValuePair;
import org.jruby.util.cli.Options;

public class RubyParser {
    protected ParserSupport support;
    protected RubyLexer lexer;

    public RubyParser() {
        this(new ParserSupport());
    }

    public RubyParser(ParserSupport support) {
        this.support = support;
        lexer = new RubyLexer();
        lexer.setParserSupport(support);
        support.setLexer(lexer);
    }

    public void setWarnings(IRubyWarnings warnings) {
        support.setWarnings(warnings);
        lexer.setWarnings(warnings);
    }
					// line 151 "-"
  // %token constants
  public static final int kCLASS = 257;
  public static final int kMODULE = 258;
  public static final int kDEF = 259;
  public static final int kUNDEF = 260;
  public static final int kBEGIN = 261;
  public static final int kRESCUE = 262;
  public static final int kENSURE = 263;
  public static final int kEND = 264;
  public static final int kIF = 265;
  public static final int kUNLESS = 266;
  public static final int kTHEN = 267;
  public static final int kELSIF = 268;
  public static final int kELSE = 269;
  public static final int kCASE = 270;
  public static final int kWHEN = 271;
  public static final int kWHILE = 272;
  public static final int kUNTIL = 273;
  public static final int kFOR = 274;
  public static final int kBREAK = 275;
  public static final int kNEXT = 276;
  public static final int kREDO = 277;
  public static final int kRETRY = 278;
  public static final int kIN = 279;
  public static final int kDO = 280;
  public static final int kDO_COND = 281;
  public static final int kDO_BLOCK = 282;
  public static final int kRETURN = 283;
  public static final int kYIELD = 284;
  public static final int kSUPER = 285;
  public static final int kSELF = 286;
  public static final int kNIL = 287;
  public static final int kTRUE = 288;
  public static final int kFALSE = 289;
  public static final int kAND = 290;
  public static final int kOR = 291;
  public static final int kNOT = 292;
  public static final int kIF_MOD = 293;
  public static final int kUNLESS_MOD = 294;
  public static final int kWHILE_MOD = 295;
  public static final int kUNTIL_MOD = 296;
  public static final int kRESCUE_MOD = 297;
  public static final int kALIAS = 298;
  public static final int kDEFINED = 299;
  public static final int klBEGIN = 300;
  public static final int klEND = 301;
  public static final int k__LINE__ = 302;
  public static final int k__FILE__ = 303;
  public static final int k__ENCODING__ = 304;
  public static final int kDO_LAMBDA = 305;
  public static final int tIDENTIFIER = 306;
  public static final int tFID = 307;
  public static final int tGVAR = 308;
  public static final int tIVAR = 309;
  public static final int tCONSTANT = 310;
  public static final int tCVAR = 311;
  public static final int tLABEL = 312;
  public static final int tCHAR = 313;
  public static final int tUPLUS = 314;
  public static final int tUMINUS = 315;
  public static final int tUMINUS_NUM = 316;
  public static final int tPOW = 317;
  public static final int tCMP = 318;
  public static final int tEQ = 319;
  public static final int tEQQ = 320;
  public static final int tNEQ = 321;
  public static final int tGEQ = 322;
  public static final int tLEQ = 323;
  public static final int tANDOP = 324;
  public static final int tOROP = 325;
  public static final int tMATCH = 326;
  public static final int tNMATCH = 327;
  public static final int tDOT = 328;
  public static final int tDOT2 = 329;
  public static final int tDOT3 = 330;
  public static final int tAREF = 331;
  public static final int tASET = 332;
  public static final int tLSHFT = 333;
  public static final int tRSHFT = 334;
  public static final int tCOLON2 = 335;
  public static final int tCOLON3 = 336;
  public static final int tOP_ASGN = 337;
  public static final int tASSOC = 338;
  public static final int tLPAREN = 339;
  public static final int tLPAREN2 = 340;
  public static final int tRPAREN = 341;
  public static final int tLPAREN_ARG = 342;
  public static final int tLBRACK = 343;
  public static final int tRBRACK = 344;
  public static final int tLBRACE = 345;
  public static final int tLBRACE_ARG = 346;
  public static final int tSTAR = 347;
  public static final int tSTAR2 = 348;
  public static final int tAMPER = 349;
  public static final int tAMPER2 = 350;
  public static final int tTILDE = 351;
  public static final int tPERCENT = 352;
  public static final int tDIVIDE = 353;
  public static final int tPLUS = 354;
  public static final int tMINUS = 355;
  public static final int tLT = 356;
  public static final int tGT = 357;
  public static final int tPIPE = 358;
  public static final int tBANG = 359;
  public static final int tCARET = 360;
  public static final int tLCURLY = 361;
  public static final int tRCURLY = 362;
  public static final int tBACK_REF2 = 363;
  public static final int tSYMBEG = 364;
  public static final int tSTRING_BEG = 365;
  public static final int tXSTRING_BEG = 366;
  public static final int tREGEXP_BEG = 367;
  public static final int tWORDS_BEG = 368;
  public static final int tQWORDS_BEG = 369;
  public static final int tSTRING_DBEG = 370;
  public static final int tSTRING_DVAR = 371;
  public static final int tSTRING_END = 372;
  public static final int tLAMBDA = 373;
  public static final int tLAMBEG = 374;
  public static final int tNTH_REF = 375;
  public static final int tBACK_REF = 376;
  public static final int tSTRING_CONTENT = 377;
  public static final int tINTEGER = 378;
  public static final int tIMAGINARY = 379;
  public static final int tFLOAT = 380;
  public static final int tRATIONAL = 381;
  public static final int tREGEXP_END = 382;
  public static final int tSYMBOLS_BEG = 383;
  public static final int tQSYMBOLS_BEG = 384;
  public static final int tDSTAR = 385;
  public static final int tSTRING_DEND = 386;
  public static final int tLABEL_END = 387;
  public static final int tLOWEST = 388;
  public static final int yyErrorCode = 256;

  /** number of final state.
    */
  protected static final int yyFinal = 1;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 634
    -1,   140,     0,   133,   134,   134,   134,   134,   135,   143,
   135,    37,    36,    38,    38,    38,    38,    44,   144,    44,
   145,    39,    39,    39,    39,    39,    39,    39,    39,    39,
    39,    39,    39,    39,    39,    39,    39,    39,    39,    39,
    39,    39,    39,    39,    33,    33,    40,    40,    40,    40,
    40,    40,    45,    34,    34,    59,    59,   147,   110,   139,
    43,    43,    43,    43,    43,    43,    43,    43,    43,    43,
    43,   111,   111,   122,   122,   112,   112,   112,   112,   112,
   112,   112,   112,   112,   112,    71,    71,   100,   100,   101,
   101,    72,    72,    72,    72,    72,    72,    72,    72,    72,
    72,    72,    72,    72,    72,    72,    72,    72,    72,    72,
    77,    77,    77,    77,    77,    77,    77,    77,    77,    77,
    77,    77,    77,    77,    77,    77,    77,    77,    77,     6,
     6,    32,    32,    32,     7,     7,     7,     7,     7,   115,
   115,   116,   116,    61,   148,    61,     8,     8,     8,     8,
     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,
     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,
     8,     8,     8,     8,     8,     8,   131,   131,   131,   131,
   131,   131,   131,   131,   131,   131,   131,   131,   131,   131,
   131,   131,   131,   131,   131,   131,   131,   131,   131,   131,
   131,   131,   131,   131,   131,   131,   131,   131,   131,   131,
   131,   131,   131,   131,   131,   131,   131,   131,    41,    41,
    41,    41,    41,    41,    41,    41,    41,    41,    41,    41,
    41,    41,    41,    41,    41,    41,    41,    41,    41,    41,
    41,    41,    41,    41,    41,    41,    41,    41,    41,    41,
    41,    41,    41,    41,    41,    41,    41,    41,    41,    41,
    41,    41,    73,    76,    76,    76,    76,    53,    57,    57,
   125,   125,   125,   125,   125,    51,    51,    51,    51,    51,
   150,    55,   104,   103,   103,    79,    79,    79,    79,    35,
    35,    70,    70,    70,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,   151,    42,   152,    42,
    42,    42,    42,    42,    42,    42,    42,    42,    42,    42,
    42,    42,    42,    42,    42,    42,    42,    42,   154,   156,
    42,   157,   158,    42,    42,    42,   159,   160,    42,   161,
    42,   163,   164,    42,   165,    42,   166,    42,   167,   168,
    42,    42,    42,    42,    42,    46,   153,   153,   153,   155,
   155,    49,    49,    47,    47,   124,   124,   126,   126,    84,
    84,   127,   127,   127,   127,   127,   127,   127,   127,   127,
    91,    91,    91,    91,    90,    90,    67,    67,    67,    67,
    67,    67,    67,    67,    67,    67,    67,    67,    67,    67,
    67,    69,    69,    68,    68,    68,   119,   119,   118,   118,
   128,   128,   169,   121,    66,    66,   120,   120,   170,   109,
    58,    58,    58,    58,    22,    22,    22,    22,    22,    22,
    22,    22,    22,   171,   108,   172,   108,    74,    48,    48,
   113,   113,    75,    75,    75,    50,    50,    52,    52,    28,
    28,    28,    15,    16,    16,    16,    17,    18,    19,    25,
    25,    81,    81,    27,    27,    87,    87,    85,    85,    26,
    26,    88,    88,    80,    80,    86,    86,    20,    20,    21,
    21,    24,    24,    23,   173,    23,   174,   175,    23,    62,
    62,    62,    62,     2,     1,     1,     1,     1,    31,    29,
    29,    30,    30,    30,    30,    56,    56,    56,    56,    56,
    56,    56,    56,    56,    56,    56,    56,   114,   114,   114,
   114,   114,   114,   114,   114,   114,   114,   114,   114,    63,
    63,    54,   176,    54,    54,    65,    65,    92,    92,    92,
    92,    89,    89,    64,    64,    64,    64,    64,    64,    64,
    64,    64,    64,    64,    64,    64,    64,    64,   132,   132,
   132,   132,     9,     9,   117,   117,    82,    82,   138,    93,
    93,    94,    94,    95,    95,    96,    96,   136,   136,   137,
   137,    60,   123,   102,   102,    83,    83,    11,    11,    13,
    13,    12,    12,   107,   106,   106,    14,   177,    14,    97,
    97,    98,    98,    99,    99,    99,    99,     3,     3,     3,
     4,     4,     4,     4,     5,     5,     5,    10,    10,   141,
   141,   146,   146,   129,   130,   149,   149,   149,   162,   162,
   142,   142,    78,   105,
    }, yyLen = {
//yyLen 634
     2,     0,     2,     2,     1,     1,     3,     2,     1,     0,
     5,     4,     2,     1,     1,     3,     2,     1,     0,     5,
     0,     4,     3,     3,     3,     2,     3,     3,     3,     3,
     3,     4,     1,     3,     3,     6,     5,     5,     5,     5,
     3,     3,     3,     1,     3,     3,     1,     3,     3,     3,
     2,     1,     1,     1,     1,     1,     4,     0,     5,     1,
     2,     3,     4,     5,     4,     5,     2,     2,     2,     2,
     2,     1,     3,     1,     3,     1,     2,     3,     5,     2,
     4,     2,     4,     1,     3,     1,     3,     2,     3,     1,
     3,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     4,     3,     3,     3,     3,     2,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     4,     3,     3,     3,     3,     2,     1,     1,
     1,     2,     1,     3,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     0,     4,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     3,     5,
     3,     5,     6,     5,     5,     5,     5,     4,     3,     3,
     3,     3,     3,     3,     3,     3,     3,     4,     2,     2,
     3,     3,     3,     3,     3,     3,     3,     3,     3,     3,
     3,     3,     3,     2,     2,     3,     3,     3,     3,     3,
     6,     1,     1,     1,     2,     4,     2,     3,     1,     1,
     1,     1,     2,     4,     2,     1,     2,     2,     4,     1,
     0,     2,     2,     2,     1,     1,     2,     3,     4,     1,
     1,     3,     4,     2,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     3,     0,     3,     0,     4,
     3,     3,     2,     3,     3,     1,     4,     3,     1,     5,
     4,     3,     2,     1,     2,     2,     6,     6,     0,     0,
     7,     0,     0,     7,     5,     4,     0,     0,     9,     0,
     6,     0,     0,     8,     0,     5,     0,     6,     0,     0,
     9,     1,     1,     1,     1,     1,     1,     1,     2,     1,
     1,     1,     5,     1,     2,     1,     1,     1,     3,     1,
     3,     1,     4,     6,     3,     5,     2,     4,     1,     3,
     4,     2,     2,     1,     2,     0,     6,     8,     4,     6,
     4,     2,     6,     2,     4,     6,     2,     4,     2,     4,
     1,     1,     1,     3,     1,     4,     1,     4,     1,     3,
     1,     1,     0,     3,     4,     1,     3,     3,     0,     5,
     2,     4,     5,     5,     2,     4,     4,     3,     3,     3,
     2,     1,     4,     0,     5,     0,     5,     5,     1,     1,
     6,     0,     1,     1,     1,     2,     1,     2,     1,     1,
     1,     1,     1,     1,     1,     2,     3,     3,     3,     3,
     3,     0,     3,     1,     2,     3,     3,     0,     3,     3,
     3,     3,     3,     0,     3,     0,     3,     0,     2,     0,
     2,     0,     2,     1,     0,     3,     0,     0,     5,     1,
     1,     1,     1,     2,     1,     1,     1,     1,     3,     1,
     2,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     0,     4,     2,     3,     2,     4,     2,     2,
     1,     2,     0,     6,     8,     4,     6,     4,     6,     2,
     4,     6,     2,     4,     2,     4,     1,     0,     1,     1,
     1,     1,     1,     1,     1,     3,     1,     3,     1,     2,
     1,     2,     1,     1,     3,     1,     3,     1,     1,     2,
     1,     3,     3,     1,     3,     1,     3,     1,     1,     2,
     1,     1,     1,     2,     2,     0,     1,     0,     4,     1,
     2,     1,     3,     3,     2,     4,     2,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     0,
     1,     0,     1,     2,     2,     0,     1,     1,     1,     1,
     1,     2,     0,     0,
    }, yyDefRed = {
//yyDefRed 1086
     1,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   328,   331,     0,     0,     0,   353,   354,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     9,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   453,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   477,   479,   481,     0,     0,   412,   529,
   530,   501,   504,   502,   503,     0,     0,   450,    59,   295,
     0,   454,   296,   297,     0,   298,   299,   294,   449,   499,
   451,    32,    46,     0,     0,     0,     0,     0,     0,   302,
     0,    54,     0,     0,    85,     0,     4,   300,   301,     0,
     0,    71,     0,     2,     0,     5,     0,     7,   351,   352,
   315,     0,     0,   511,   510,   512,   513,     0,     0,   515,
   514,   516,     0,   507,   506,     0,   509,     0,     0,     0,
     0,   132,     0,   355,     0,   303,     0,   344,   186,   197,
   187,   210,   183,   203,   193,   192,   208,   191,   190,   185,
   211,   195,   184,   198,   202,   204,   196,   189,   205,   212,
   207,     0,     0,     0,     0,   182,   201,   200,   213,   214,
   215,   216,   217,   181,   188,   179,   180,     0,     0,     0,
     0,   136,     0,   171,   172,   168,   149,   150,   151,   158,
   155,   157,   152,   153,   173,   174,   159,   160,   597,   165,
   164,   148,   170,   167,   166,   162,   163,   156,   154,   146,
   169,   147,   175,   161,   346,   137,     0,   596,   138,   206,
   199,   209,   194,   176,   177,   178,   134,   135,   140,   139,
   142,     0,   141,   143,     0,     0,     0,     0,     0,     0,
    14,    13,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   628,   629,     0,     0,     0,   630,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   365,   366,     0,     0,     0,
     0,     0,   477,     0,     0,   275,    69,     0,     0,     0,
   601,   279,    70,    68,     0,    67,     0,     0,   430,    66,
     0,   622,     0,     0,    20,     0,     0,     0,   238,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   263,     0,     0,     0,   599,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   254,    50,   253,   496,   495,   497,
   493,   494,     0,     0,     0,     0,     0,     0,     0,     0,
   325,     0,     0,     0,     0,     0,   455,   435,   433,   324,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   418,   420,     0,     0,     0,   617,   618,
     0,     0,    87,     0,     0,     0,     0,     0,     0,     3,
     0,   424,     0,   322,     0,   500,     0,   129,     0,   131,
     0,   532,   339,   531,     0,     0,     0,     0,     0,     0,
   348,   144,     0,     0,     0,     0,   305,    12,     0,     0,
   357,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   631,     0,     0,     0,     0,     0,     0,
   336,   604,   286,   282,     0,   606,     0,     0,   276,   284,
     0,   277,     0,   317,     0,   281,   271,   270,     0,     0,
     0,     0,   321,    49,    22,    24,    23,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   310,     0,     0,
   307,   313,     0,   626,   264,     0,   266,   314,   600,     0,
    89,     0,     0,     0,     0,     0,   486,   484,   498,   483,
   480,   456,   478,   457,   458,   482,   459,   460,   463,     0,
   469,   470,     0,   563,   560,   559,   558,   561,   568,   577,
     0,     0,   588,   587,   592,   591,   578,     0,     0,     0,
     0,   585,   415,     0,     0,     0,   556,   575,     0,   540,
   566,   562,     0,     0,     0,   465,   466,     0,   471,   472,
     0,     0,     0,    26,    27,    28,    29,    30,    47,    48,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   612,     0,
     0,   613,   428,     0,     0,     0,     0,   427,     0,   429,
     0,   610,   611,     0,    40,     0,     0,    45,    44,     0,
    41,   285,     0,     0,     0,     0,     0,    88,    33,    42,
   289,     0,    34,     0,     6,    57,    61,     0,   534,     0,
     0,     0,     0,     0,     0,   133,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   443,     0,     0,   444,     0,
     0,   363,    15,     0,   358,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   335,   360,   329,   359,   332,     0,
     0,     0,     0,     0,     0,     0,   603,     0,     0,     0,
   283,   602,   316,   623,     0,     0,   267,   320,    21,     0,
     0,    31,     0,     0,     0,   309,     0,     0,     0,     0,
     0,     0,     0,     0,   487,     0,   462,   464,   474,     0,
     0,   367,     0,   369,     0,     0,     0,   589,   593,     0,
   554,     0,     0,   413,     0,   549,     0,   552,     0,   538,
   579,     0,   539,   569,   468,   476,   404,     0,   402,     0,
   401,     0,     0,     0,     0,     0,   269,     0,   425,   268,
     0,     0,   426,     0,     0,     0,     0,     0,     0,     0,
     0,     0,    86,     0,     0,     0,     0,   342,     0,     0,
   432,   345,   598,     0,   536,     0,   349,   145,     0,     0,
     0,   446,   364,     0,    11,   448,     0,   361,     0,     0,
     0,     0,     0,     0,     0,   334,     0,     0,     0,     0,
     0,     0,   605,   288,   278,     0,   319,    10,   265,    90,
     0,     0,   489,   490,   491,   485,   492,     0,     0,     0,
     0,   565,     0,     0,   581,   564,     0,   541,     0,     0,
     0,     0,   567,     0,   586,     0,   576,   594,     0,     0,
     0,     0,     0,   400,   573,     0,     0,   383,     0,   583,
     0,     0,     0,     0,     0,     0,    36,     0,    37,     0,
    63,    39,     0,    38,     0,    65,     0,   624,   423,   422,
     0,     0,     0,     0,     0,     0,     0,   533,   340,   535,
   347,     0,    19,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   445,     0,
   447,     0,   326,     0,   327,   287,     0,     0,     0,   337,
     0,     0,   368,     0,     0,     0,   370,   414,     0,     0,
   555,   417,   416,     0,   547,     0,   545,     0,   550,   553,
   537,     0,     0,   398,     0,     0,   393,     0,   381,     0,
   396,   403,   382,     0,     0,     0,     0,   436,   434,     0,
   419,    35,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   438,   437,   439,   330,   333,     0,   488,
     0,     0,     0,     0,   410,     0,   408,   411,     0,     0,
     0,     0,     0,     0,   384,   405,     0,     0,   574,     0,
     0,     0,   584,   312,     0,    58,   343,     0,     0,     0,
     0,     0,     0,   440,     0,     0,     0,     0,     0,   407,
   548,     0,   543,   546,   551,     0,   399,     0,   390,     0,
   388,   380,     0,   394,   397,     0,     0,   350,     0,   362,
   338,     0,   409,     0,     0,     0,     0,     0,   544,   392,
     0,   386,   389,   395,     0,   387,
    }, yyDgoto = {
//yyDgoto 178
     1,   360,    67,    68,   674,   637,   131,   229,   631,   865,
   420,   568,   569,   570,   216,    69,    70,    71,    72,    73,
   363,   362,    74,   540,   365,    75,    76,   549,    77,    78,
    79,    80,   132,    81,    82,   659,   236,   237,   238,   239,
    84,    85,    86,    87,   240,   256,   319,   827,  1004,   828,
   820,   496,   824,   639,   442,   305,    89,   788,    90,    91,
   571,   231,   855,   258,   680,   681,   573,   881,   778,   779,
   650,    93,    94,   297,   472,   687,   329,   259,   241,   498,
   369,   367,   574,   575,   752,   373,   375,    97,    98,   760,
   973,  1024,   867,   577,   884,   885,   578,   335,   499,   300,
    99,   531,   886,   488,   301,   489,   769,   579,   433,   414,
   666,   100,   101,   455,   260,   232,   233,   580,  1015,   862,
   763,   370,   326,   889,   287,   500,   753,   754,  1016,   493,
   794,   218,   581,   103,   104,   105,   582,   583,   584,   136,
     2,   265,   266,   316,   453,   507,   494,   806,   683,   524,
   306,   328,   519,   461,   268,   706,   838,   269,   839,   714,
  1008,   670,   462,   667,   916,   447,   449,   682,   921,   371,
   626,   592,   591,   745,   744,   851,   669,   448,
    }, yySindex = {
//yySindex 1086
     0,     0, 17809, 19098, 20762, 21146, 17247, 17583, 17938, 20250,
 20250,  7867,     0,     0, 20890, 18195, 18195,     0,     0, 18195,
  -205,  -152,     0,     0,     0,     0,    79, 17471,   259,     0,
   -61,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0, 20378, 20378,   949,     6, 18067, 20250, 18582, 18969,  5259,
 20378, 20506, 17359,     0,     0,     0,   297,   300,     0,     0,
     0,     0,     0,     0,     0,   317,   320,     0,     0,     0,
    37,     0,     0,     0,  -174,     0,     0,     0,     0,     0,
     0,     0,     0,  1273,   -44, 16326,     0,    80,   -15,     0,
  -150,     0,   124,   325,     0,   313,     0,     0,     0, 21018,
   398,     0,   147,     0,   162,     0,  -179,     0,     0,     0,
     0,  -205,  -152,     0,     0,     0,     0,   149,   259,     0,
     0,     0,     0,     0,     0,     0,     0,   949, 20250,  -140,
 17938,     0,   184,     0,   460,     0,  -179,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,  -150,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,   456,     0,     0, 19226, 17938,   244,   251,   162,  1273,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   211,   -44,    83,   511,   186,   475,
   261,    83,     0,     0,   162,   379,   595,     0, 20250, 20250,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   395,   571,     0,     0,     0,   428, 20378, 20378,
 20378, 20378,     0, 20378, 16326,     0,     0,   258,   667,   675,
     0,     0,     0,     0,  6692,     0, 18195, 18195,     0,     0,
  7995,     0, 20250,  -148,     0, 19354,   366, 17938,     0,   598,
   415,   416,   401, 18067,   394,     0,   259,   -44,   259,   407,
     0,   121,   167,   258,     0,   393,   167,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,   447,
 21274,   611,     0,   720,     0,     0,     0,     0,     0,     0,
     0,     0,   537,   711,   752,   862,   396,   830,   397,   -89,
     0,  3156,   411,   926,   414,   -75,     0,     0,     0,     0,
 20250, 20250, 20250, 20250, 19226, 20250, 20250, 20378, 20378, 20378,
 20378, 20378, 20378, 20378, 20378, 20378, 20378, 20378, 20378, 20378,
 20378, 20378, 20378, 20378, 20378, 20378, 20378, 20378, 20378, 20378,
 20378, 20378, 20378,     0,     0,  2720,  3643, 18195,     0,     0,
  5770, 20506,     0, 19482, 18067, 16942,   729, 19482, 20506,     0,
  4779,     0,   443,     0,   451,     0,   -44,     0,     0,     0,
   162,     0,     0,     0,  3733,  4150, 18195, 17938, 20250,  3168,
     0,     0,  1273,   446, 19610,   544,     0,     0, 17070,   401,
     0, 17938,   547,  4234,  5642, 18195, 20378, 20378, 20378, 17938,
   379, 19738,   554,     0,    67,    67,     0,  5715,  6272, 18195,
     0,     0,     0,     0,   369,     0, 20378, 18324,     0,     0,
 18711,     0,   259,     0,   478,     0,     0,     0,   779,   784,
   259,    54,     0,     0,     0,     0,     0, 17583, 20250, 16326,
 17809,   479,  4234,  5642, 20378, 20378,   259,     0,     0,   259,
     0,     0, 18840,     0,     0, 18969,     0,     0,     0,     0,
     0,   801, 21609, 21664, 18195, 21274,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,    12,
     0,     0,   826,     0,     0,     0,     0,     0,     0,     0,
  1117,  2648,     0,     0,     0,     0,     0,   790,   553,   560,
   809,     0,     0,  -137,   823,   829,     0,     0,   831,     0,
     0,     0,   574,   833, 20378,     0,     0,   182,     0,     0,
   852,  -191,  -191,     0,     0,     0,     0,     0,     0,     0,
   415,  3323,  3323,  3323,  3323,  2815,  2815,  3811,  3310,  3323,
  3323,  2804,  2804,   984,   984,   415,  2176,   415,   415,   348,
   348,  2815,  2815,  2667,  2667,  4647,  -191,   551,     0,   552,
  -152,     0,     0,   556,     0,   557,  -152,     0,     0,     0,
   259,     0,     0,  -152,     0, 16326, 20378,     0,     0,  6203,
     0,     0,   836,   846,   259, 21274,   861,     0,     0,     0,
     0,     0,     0, 17143,     0,     0,     0,   162,     0, 20250,
 17938,  -152,     0,     0,  -152,     0,   259,   653,    54,  2648,
   162, 17938, 17695, 17583, 17809,     0,     0,   587,     0, 17938,
   660,     0,     0,   390,     0,   591,   594,   610,   613,   259,
  6203,   544,   689,   103,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   259, 20250, 20378,     0, 20378,   258,   675,
     0,     0,     0,     0, 18324, 18711,     0,     0,     0,    54,
   590,     0,   415, 16326,     0,     0,   167, 21274,     0,     0,
     0,     0,   259,   801,     0,  1014,     0,     0,     0,  1117,
   626,     0,   911,     0,   259,   259, 20378,     0,     0,  3143,
     0, 17938, 17938,     0,  2648,     0,  2648,     0,   -95,     0,
     0,   -56,     0,     0,     0,     0,     0,    49,     0, 17938,
     0, 17938,   899, 17938, 20506, 20506,     0,   443,     0,     0,
 20506, 20506,     0,   443,   621,   615,    80,  -174,     0, 20378,
 20506, 19866,     0,   801, 21274, 20378,  -191,     0,   162,   697,
     0,     0,     0,   259,     0,   698,     0,     0,   601, 21402,
    83,     0,     0, 17938,     0,     0, 20250,     0,   709, 20378,
 20378, 20378, 20378,   638,   715,     0, 19994, 17938, 17938, 17938,
     0,    67,     0,     0,     0,   937,     0,     0,     0,     0,
     0, 17938,     0,     0,     0,     0,     0,   259,  1553,   944,
  1710,     0,   648,   934,     0,     0,   955,     0,   739,   643,
   965,   970,     0,   974,     0,   955,     0,     0,   833,   966,
   988,   259,   990,     0,     0,   991,   993,     0,   681,     0,
   833, 21530,   788,   694, 20378,   795,     0, 16326,     0, 16326,
     0,     0, 16326,     0, 16326,     0, 20506,     0,     0,     0,
 16326, 20378,     0,   801, 16326, 17938, 17938,     0,     0,     0,
     0,  3168,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   759,   687,     0,     0, 17938,
     0,    83,     0, 20378,     0,     0,   122,   798,   806,     0,
 18711,   718,     0,  1033,  1553,   632,     0,     0,  1887,  3143,
     0,     0,     0,  3143,     0,  2648,     0,  3143,     0,     0,
     0, 21530,  3143,     0,   736,  3221,     0,   -95,     0,  3221,
     0,     0,     0,     0,     0,   799,   712,     0,     0, 16326,
     0,     0, 16326,     0,   743,   847, 17938,     0, 21719, 21774,
 18195,   244, 17938,     0,     0,     0,     0,     0, 17938,     0,
  1553,  1033,  1553,  1070,     0,   169,     0,     0,   955,  1081,
   955,   955,   712,  1086,     0,     0,  1094,  1096,     0,   833,
  1097,  1086,     0,     0, 21829,     0,     0,   880,     0,     0,
     0,     0,   259,     0,   390,   886,  1033,  1553,  1887,     0,
     0,  3143,     0,     0,     0,  3143,     0,  3143,     0,  3221,
     0,     0,  3143,     0,     0,     0,     0,     0,     0,     0,
     0,  1033,     0,   955,  1086,  1111,  1086,  1086,     0,     0,
  3143,     0,     0,     0,  1086,     0,
    }, yyRindex = {
//yyRindex 1086
     0,     0,   164,     0,     0,     0,     0,     0,  1744,     0,
     0,   892,     0,     0,     0, 14598, 14709,     0,     0, 14811,
  5060,  4559, 15099, 15237, 15313, 15431, 20634,     0, 20122,     0,
     0, 15601, 15719, 15795,  5421,  3557, 15901, 16083,  5550, 16189,
     0,     0,     0,     0,     0,   214,   145,   827,   807,   108,
     0,     0,   943,     0,     0,     0,   980,   -49,     0,     0,
     0,     0,     0,     0,     0,  1284,   -26,     0,     0,     0,
 10030,     0,     0,     0, 10162,     0,     0,     0,     0,     0,
     0,     0,     0,    65,   994,  1609, 10271,  8649,     0,     0,
  8691,     0, 16265,     0,     0,     0,     0,     0,     0,   260,
     0,     0,     0,     0,    42,     0, 18453,     0,     0,     0,
     0, 10379,  8286,     0,     0,     0,     0,     0,   851,     0,
     0,     0,  6824,     0,     0,  6953,     0,     0,     0,     0,
   214,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  1069,  1289,  1336,  1504,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,  1606,  1750,  1917,
  2262,     0,  2664,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  1449,     0,     0,     0,    74,    43,     0,  1184,    62,
     0,     0,  8399,  8526,  8766,  8875,  8983,  9115,  9224,  2423,
  9332,  9464,  2555,  9573,     0, 16207,     0,     0,  9813,     0,
     0,     0,     0,     0,   892,     0,   902,     0,     0,     0,
   792,   811,  1123,  1152,  1607,  1661,  1664,  2115,  1875,  1933,
  2302,  2306,     0,     0,  2327,     0,     0,     0,     0,     0,
     0,     0,     0,     0, 14359,     0,     0, 16021, 16521, 16521,
     0,     0,     0,     0,   864,     0,     0,   216,     0,     0,
   864,     0,     0,     0,     0,     0,     0,    39,     0,     0,
 10620, 10511, 16371,   214,     0,    59,   864,   238,   864,     0,
     0,   867,   867,     0,     0,     0,   853,   825,  1160,  1514,
  1616,  1704,  1903,  2160,  1203,  5424,  5609,  1354,  6054,     0,
     0,     0,  7085,   286,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  -132,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,    41,     0,     0,
     0,     0,     0,     0,   214,   346,   384,     0,     0,     0,
    58,     0, 16626,     0,     0,     0,   166,     0,  7340,     0,
     0,     0,     0,     0,     0,     0,    41,  1744,     0,   190,
     0,     0,   844,     0,   573,   429,     0,     0,  2201,  9922,
     0,  1363,  7469,     0,     0,    41,     0,     0,     0,   516,
     0,     0,     0,     0,     0,     0,  4768,     0,     0,    41,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   864,     0,     0,     0,     0,     0,    51,    51,
   864,   864,     0,     0,     0,     0,     0,     0,     0, 13424,
    39,     0,     0,     0,     0,     0,   864,     0,    35,   864,
     0,     0,   873,     0,     0,   -63,     0,     0,     0,  7214,
     0,   533,     0,     0,    41,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,   220,     0,     0,     0,     0,     0,   206,   161,     0,
   176,     0,     0,     0,   176,   176,     0,     0,   281,     0,
     0,     0,   200,   281,   208,     0,     0,     0,     0,     0,
     0,  7598,  7738,     0,     0,     0,     0,     0,     0,     0,
 10728, 12565, 12652, 12741, 12838, 12110, 12228, 12925, 13198, 13015,
 13111, 13288, 13328, 11544, 11653, 10860, 11773, 10969, 11077, 11318,
 11436, 12351, 12468, 11893, 12002,  1150,  7598,  5922,     0,  6051,
  4931,     0,     0,  6423,  3929,  6552, 18453,     0,  4058,     0,
   875,     0,     0,  2023,     0, 13509,     0,     0,     0, 16902,
     0,     0,     0,     0,   864,     0,   546,     0,     0,     0,
     0, 13236,     0, 14417,     0,     0,     0,     0,     0,     0,
  1744,  9681,  7082,  7211,     0,     0,   875,     0,   864,   243,
     0,  1744,     0,     0,    39,     0,   309,    97,     0,   585,
   964,     0,     0,   964,     0,  2927,  3056,  3428,  4430,   875,
 14502,   964,     0,     0,     0,     0,     0,     0,     0,  3265,
  3766,  4267,   672,   875,     0,     0,     0,     0, 16481, 16521,
     0,     0,     0,     0,   131,   138,     0,     0,     0,   864,
     0,     0, 11195, 13594,   197,     0,   867,     0,  1459,  1496,
  2133,   821,   875,   581,     0,     0,     0,     0,     0,     0,
   256,     0,   262,     0,   864,    26,     0,     0,     0,     0,
     0,    90,    39,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,    32,     0,    90,
     0,    39,     0,    90,     0,     0,     0, 16663,     0,     0,
     0,     0,     0, 16746, 14949,     0, 15117,  2163, 15175,     0,
     0,     0,     0,   651,     0,     0,  7738,     0,     0,     0,
     0,     0,     0,   864,     0,     0,     0,     0,     0,     0,
     0,     0,     0,    90,     0,     0,     0,     0,     0,     0,
     0,     0,     0,  8158,     0,     0,     0,  1062,    90,    90,
  2172,     0,     0,     0,     0,    51,     0,     0,     0,     0,
   502,    39,     0,     0,     0,     0,     0,   864,     0,   264,
     0,     0,     0,  -201,     0,     0,   176,     0,     0,     0,
   176,   176,     0,   176,     0,   176,     0,     0,   281,    73,
    68,    32,    68,     0,     0,    71,    68,     0,     0,     0,
    71,    92,     0,     0,     0,     0,     0, 13679,     0, 13764,
     0,     0, 13849,     0, 13934,     0,     0,     0,     0,     0,
 14019,     0, 16786,   688, 14104,    39,  1744,     0,     0,     0,
     0,   190,     0,  1057,  1217,  1457,  1512,  1715,  1767,  1914,
   437,  2012,  2081,   819,  2094,     0,     0,  2150,     0,  1744,
     0,     0,     0,     0,     0,     0,   964,     0,     0,     0,
   144,     0,     0,   277,     0,   279,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,    99,     0,     0,     0,     0,
     0,     0,     0,   910,  1133,     0,    93,     0,     0, 14189,
     0,     0, 14274, 16798,     0,     0,  1744,  2180,     0,     0,
    41,    43,  1363,     0,     0,     0,     0,     0,    90,     0,
     0,   282,     0,   285,     0,   -81,     0,     0,   176,   176,
   176,   176,   100,    68,     0,     0,    68,    68,     0,    71,
    68,    68,     0,     0,     0,     0,     0,     0,  1026,  1219,
  1640,   693,   875,     0,   964,     0,   288,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,  1225,   761,     0,  1006,     0,
     0,   298,     0,   176,    68,    68,    68,    68,     0,     0,
     0,     0,     0,     0,    68,     0,
    }, yyGindex = {
//yyGindex 178
     0,     0,    23,     0,  -349,     0,   -59,     4,    -4,   -36,
  1015,     0,     0,   459,     0,     0,     0,  1164,     0,     0,
   945,  1192,     0,  1248,     0,     0,     0,   876,     0,     0,
   102,    27,  1243,  -390,     9,     0,   521,  -436,     0,    34,
   362,  1721,    13,    -3,   794,    -5,   187,  -430,     0,   209,
     0,   760,     0,    10,     0,    47,  1249,   617,     0,     0,
  -680,     0,     0,  1046,  -196,   345,     0,     0,     0,  -488,
  -176,   -70,    -9,    91,  -448,     0,     0,   403,    -2,   165,
     0,     0, 11721,   503,    63,     0,     0,     0,     0,    25,
  2059,   493,  -357,   504,   315,     0,     0,     0,     5,  -443,
     0,  -462,   299,  -291,  -449,     0,  -551,  1298,   -73,   499,
  -531,  1261,    18,   304,  1149,     0,   -12,  -244,     0,  -666,
     0,     0,  -215,  -860,     0,  -391,  -770,   558,   263,   464,
  -619,     0,  -829,  -423,     0,    16,     0,  2522,  1564,   751,
     0,    46,     1,     0,     0,     0,   -19,     0,     0,  -297,
     0,     0,     0,  -233,     0,  -432,     0,     0,     0,     0,
     0,     0,    -7,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,
    };
    protected static final short[] yyTable = YyTables.yyTable();
    protected static final short[] yyCheck = YyTables.yyCheck();

  /** maps symbol value to printable name.
      @see #yyExpecting
    */
  protected static final String[] yyNames = {
    "end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,"' '",null,null,null,null,null,
    null,null,null,null,null,null,"','",null,null,null,null,null,null,
    null,null,null,null,null,null,null,"':'","';'",null,"'='",null,"'?'",
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,
    "'['",null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,
    "kCLASS","kMODULE","kDEF","kUNDEF","kBEGIN","kRESCUE","kENSURE",
    "kEND","kIF","kUNLESS","kTHEN","kELSIF","kELSE","kCASE","kWHEN",
    "kWHILE","kUNTIL","kFOR","kBREAK","kNEXT","kREDO","kRETRY","kIN",
    "kDO","kDO_COND","kDO_BLOCK","kRETURN","kYIELD","kSUPER","kSELF",
    "kNIL","kTRUE","kFALSE","kAND","kOR","kNOT","kIF_MOD","kUNLESS_MOD",
    "kWHILE_MOD","kUNTIL_MOD","kRESCUE_MOD","kALIAS","kDEFINED","klBEGIN",
    "klEND","k__LINE__","k__FILE__","k__ENCODING__","kDO_LAMBDA",
    "tIDENTIFIER","tFID","tGVAR","tIVAR","tCONSTANT","tCVAR","tLABEL",
    "tCHAR","tUPLUS","tUMINUS","tUMINUS_NUM","tPOW","tCMP","tEQ","tEQQ",
    "tNEQ","tGEQ","tLEQ","tANDOP","tOROP","tMATCH","tNMATCH","tDOT",
    "tDOT2","tDOT3","tAREF","tASET","tLSHFT","tRSHFT","tCOLON2","tCOLON3",
    "tOP_ASGN","tASSOC","tLPAREN","tLPAREN2","tRPAREN","tLPAREN_ARG",
    "tLBRACK","tRBRACK","tLBRACE","tLBRACE_ARG","tSTAR","tSTAR2","tAMPER",
    "tAMPER2","tTILDE","tPERCENT","tDIVIDE","tPLUS","tMINUS","tLT","tGT",
    "tPIPE","tBANG","tCARET","tLCURLY","tRCURLY","tBACK_REF2","tSYMBEG",
    "tSTRING_BEG","tXSTRING_BEG","tREGEXP_BEG","tWORDS_BEG","tQWORDS_BEG",
    "tSTRING_DBEG","tSTRING_DVAR","tSTRING_END","tLAMBDA","tLAMBEG",
    "tNTH_REF","tBACK_REF","tSTRING_CONTENT","tINTEGER","tIMAGINARY",
    "tFLOAT","tRATIONAL","tREGEXP_END","tSYMBOLS_BEG","tQSYMBOLS_BEG",
    "tDSTAR","tSTRING_DEND","tLABEL_END","tLOWEST",
    };

  /** printable rules for debugging.
    */
  protected static final String [] yyRule = {
    "$accept : program",
    "$$1 :",
    "program : $$1 top_compstmt",
    "top_compstmt : top_stmts opt_terms",
    "top_stmts : none",
    "top_stmts : top_stmt",
    "top_stmts : top_stmts terms top_stmt",
    "top_stmts : error top_stmt",
    "top_stmt : stmt",
    "$$2 :",
    "top_stmt : klBEGIN $$2 tLCURLY top_compstmt tRCURLY",
    "bodystmt : compstmt opt_rescue opt_else opt_ensure",
    "compstmt : stmts opt_terms",
    "stmts : none",
    "stmts : stmt_or_begin",
    "stmts : stmts terms stmt_or_begin",
    "stmts : error stmt",
    "stmt_or_begin : stmt",
    "$$3 :",
    "stmt_or_begin : kBEGIN $$3 tLCURLY top_compstmt tRCURLY",
    "$$4 :",
    "stmt : kALIAS fitem $$4 fitem",
    "stmt : kALIAS tGVAR tGVAR",
    "stmt : kALIAS tGVAR tBACK_REF",
    "stmt : kALIAS tGVAR tNTH_REF",
    "stmt : kUNDEF undef_list",
    "stmt : stmt kIF_MOD expr_value",
    "stmt : stmt kUNLESS_MOD expr_value",
    "stmt : stmt kWHILE_MOD expr_value",
    "stmt : stmt kUNTIL_MOD expr_value",
    "stmt : stmt kRESCUE_MOD stmt",
    "stmt : klEND tLCURLY compstmt tRCURLY",
    "stmt : command_asgn",
    "stmt : mlhs '=' command_call",
    "stmt : var_lhs tOP_ASGN command_call",
    "stmt : primary_value '[' opt_call_args rbracket tOP_ASGN command_call",
    "stmt : primary_value tDOT tIDENTIFIER tOP_ASGN command_call",
    "stmt : primary_value tDOT tCONSTANT tOP_ASGN command_call",
    "stmt : primary_value tCOLON2 tCONSTANT tOP_ASGN command_call",
    "stmt : primary_value tCOLON2 tIDENTIFIER tOP_ASGN command_call",
    "stmt : backref tOP_ASGN command_call",
    "stmt : lhs '=' mrhs",
    "stmt : mlhs '=' mrhs_arg",
    "stmt : expr",
    "command_asgn : lhs '=' command_call",
    "command_asgn : lhs '=' command_asgn",
    "expr : command_call",
    "expr : expr kAND expr",
    "expr : expr kOR expr",
    "expr : kNOT opt_nl expr",
    "expr : tBANG command_call",
    "expr : arg",
    "expr_value : expr",
    "command_call : command",
    "command_call : block_command",
    "block_command : block_call",
    "block_command : block_call dot_or_colon operation2 command_args",
    "$$5 :",
    "cmd_brace_block : tLBRACE_ARG $$5 opt_block_param compstmt tRCURLY",
    "fcall : operation",
    "command : fcall command_args",
    "command : fcall command_args cmd_brace_block",
    "command : primary_value tDOT operation2 command_args",
    "command : primary_value tDOT operation2 command_args cmd_brace_block",
    "command : primary_value tCOLON2 operation2 command_args",
    "command : primary_value tCOLON2 operation2 command_args cmd_brace_block",
    "command : kSUPER command_args",
    "command : kYIELD command_args",
    "command : kRETURN call_args",
    "command : kBREAK call_args",
    "command : kNEXT call_args",
    "mlhs : mlhs_basic",
    "mlhs : tLPAREN mlhs_inner rparen",
    "mlhs_inner : mlhs_basic",
    "mlhs_inner : tLPAREN mlhs_inner rparen",
    "mlhs_basic : mlhs_head",
    "mlhs_basic : mlhs_head mlhs_item",
    "mlhs_basic : mlhs_head tSTAR mlhs_node",
    "mlhs_basic : mlhs_head tSTAR mlhs_node ',' mlhs_post",
    "mlhs_basic : mlhs_head tSTAR",
    "mlhs_basic : mlhs_head tSTAR ',' mlhs_post",
    "mlhs_basic : tSTAR mlhs_node",
    "mlhs_basic : tSTAR mlhs_node ',' mlhs_post",
    "mlhs_basic : tSTAR",
    "mlhs_basic : tSTAR ',' mlhs_post",
    "mlhs_item : mlhs_node",
    "mlhs_item : tLPAREN mlhs_inner rparen",
    "mlhs_head : mlhs_item ','",
    "mlhs_head : mlhs_head mlhs_item ','",
    "mlhs_post : mlhs_item",
    "mlhs_post : mlhs_post ',' mlhs_item",
    "mlhs_node : tIDENTIFIER",
    "mlhs_node : tIVAR",
    "mlhs_node : tGVAR",
    "mlhs_node : tCONSTANT",
    "mlhs_node : tCVAR",
    "mlhs_node : kNIL",
    "mlhs_node : kSELF",
    "mlhs_node : kTRUE",
    "mlhs_node : kFALSE",
    "mlhs_node : k__FILE__",
    "mlhs_node : k__LINE__",
    "mlhs_node : k__ENCODING__",
    "mlhs_node : primary_value '[' opt_call_args rbracket",
    "mlhs_node : primary_value tDOT tIDENTIFIER",
    "mlhs_node : primary_value tCOLON2 tIDENTIFIER",
    "mlhs_node : primary_value tDOT tCONSTANT",
    "mlhs_node : primary_value tCOLON2 tCONSTANT",
    "mlhs_node : tCOLON3 tCONSTANT",
    "mlhs_node : backref",
    "lhs : tIDENTIFIER",
    "lhs : tIVAR",
    "lhs : tGVAR",
    "lhs : tCONSTANT",
    "lhs : tCVAR",
    "lhs : kNIL",
    "lhs : kSELF",
    "lhs : kTRUE",
    "lhs : kFALSE",
    "lhs : k__FILE__",
    "lhs : k__LINE__",
    "lhs : k__ENCODING__",
    "lhs : primary_value '[' opt_call_args rbracket",
    "lhs : primary_value tDOT tIDENTIFIER",
    "lhs : primary_value tCOLON2 tIDENTIFIER",
    "lhs : primary_value tDOT tCONSTANT",
    "lhs : primary_value tCOLON2 tCONSTANT",
    "lhs : tCOLON3 tCONSTANT",
    "lhs : backref",
    "cname : tIDENTIFIER",
    "cname : tCONSTANT",
    "cpath : tCOLON3 cname",
    "cpath : cname",
    "cpath : primary_value tCOLON2 cname",
    "fname : tIDENTIFIER",
    "fname : tCONSTANT",
    "fname : tFID",
    "fname : op",
    "fname : reswords",
    "fsym : fname",
    "fsym : symbol",
    "fitem : fsym",
    "fitem : dsym",
    "undef_list : fitem",
    "$$6 :",
    "undef_list : undef_list ',' $$6 fitem",
    "op : tPIPE",
    "op : tCARET",
    "op : tAMPER2",
    "op : tCMP",
    "op : tEQ",
    "op : tEQQ",
    "op : tMATCH",
    "op : tNMATCH",
    "op : tGT",
    "op : tGEQ",
    "op : tLT",
    "op : tLEQ",
    "op : tNEQ",
    "op : tLSHFT",
    "op : tRSHFT",
    "op : tDSTAR",
    "op : tPLUS",
    "op : tMINUS",
    "op : tSTAR2",
    "op : tSTAR",
    "op : tDIVIDE",
    "op : tPERCENT",
    "op : tPOW",
    "op : tBANG",
    "op : tTILDE",
    "op : tUPLUS",
    "op : tUMINUS",
    "op : tAREF",
    "op : tASET",
    "op : tBACK_REF2",
    "reswords : k__LINE__",
    "reswords : k__FILE__",
    "reswords : k__ENCODING__",
    "reswords : klBEGIN",
    "reswords : klEND",
    "reswords : kALIAS",
    "reswords : kAND",
    "reswords : kBEGIN",
    "reswords : kBREAK",
    "reswords : kCASE",
    "reswords : kCLASS",
    "reswords : kDEF",
    "reswords : kDEFINED",
    "reswords : kDO",
    "reswords : kELSE",
    "reswords : kELSIF",
    "reswords : kEND",
    "reswords : kENSURE",
    "reswords : kFALSE",
    "reswords : kFOR",
    "reswords : kIN",
    "reswords : kMODULE",
    "reswords : kNEXT",
    "reswords : kNIL",
    "reswords : kNOT",
    "reswords : kOR",
    "reswords : kREDO",
    "reswords : kRESCUE",
    "reswords : kRETRY",
    "reswords : kRETURN",
    "reswords : kSELF",
    "reswords : kSUPER",
    "reswords : kTHEN",
    "reswords : kTRUE",
    "reswords : kUNDEF",
    "reswords : kWHEN",
    "reswords : kYIELD",
    "reswords : kIF_MOD",
    "reswords : kUNLESS_MOD",
    "reswords : kWHILE_MOD",
    "reswords : kUNTIL_MOD",
    "reswords : kRESCUE_MOD",
    "arg : lhs '=' arg",
    "arg : lhs '=' arg kRESCUE_MOD arg",
    "arg : var_lhs tOP_ASGN arg",
    "arg : var_lhs tOP_ASGN arg kRESCUE_MOD arg",
    "arg : primary_value '[' opt_call_args rbracket tOP_ASGN arg",
    "arg : primary_value tDOT tIDENTIFIER tOP_ASGN arg",
    "arg : primary_value tDOT tCONSTANT tOP_ASGN arg",
    "arg : primary_value tCOLON2 tIDENTIFIER tOP_ASGN arg",
    "arg : primary_value tCOLON2 tCONSTANT tOP_ASGN arg",
    "arg : tCOLON3 tCONSTANT tOP_ASGN arg",
    "arg : backref tOP_ASGN arg",
    "arg : arg tDOT2 arg",
    "arg : arg tDOT3 arg",
    "arg : arg tPLUS arg",
    "arg : arg tMINUS arg",
    "arg : arg tSTAR2 arg",
    "arg : arg tDIVIDE arg",
    "arg : arg tPERCENT arg",
    "arg : arg tPOW arg",
    "arg : tUMINUS_NUM simple_numeric tPOW arg",
    "arg : tUPLUS arg",
    "arg : tUMINUS arg",
    "arg : arg tPIPE arg",
    "arg : arg tCARET arg",
    "arg : arg tAMPER2 arg",
    "arg : arg tCMP arg",
    "arg : arg tGT arg",
    "arg : arg tGEQ arg",
    "arg : arg tLT arg",
    "arg : arg tLEQ arg",
    "arg : arg tEQ arg",
    "arg : arg tEQQ arg",
    "arg : arg tNEQ arg",
    "arg : arg tMATCH arg",
    "arg : arg tNMATCH arg",
    "arg : tBANG arg",
    "arg : tTILDE arg",
    "arg : arg tLSHFT arg",
    "arg : arg tRSHFT arg",
    "arg : arg tANDOP arg",
    "arg : arg tOROP arg",
    "arg : kDEFINED opt_nl arg",
    "arg : arg '?' arg opt_nl ':' arg",
    "arg : primary",
    "arg_value : arg",
    "aref_args : none",
    "aref_args : args trailer",
    "aref_args : args ',' assocs trailer",
    "aref_args : assocs trailer",
    "paren_args : tLPAREN2 opt_call_args rparen",
    "opt_paren_args : none",
    "opt_paren_args : paren_args",
    "opt_call_args : none",
    "opt_call_args : call_args",
    "opt_call_args : args ','",
    "opt_call_args : args ',' assocs ','",
    "opt_call_args : assocs ','",
    "call_args : command",
    "call_args : args opt_block_arg",
    "call_args : assocs opt_block_arg",
    "call_args : args ',' assocs opt_block_arg",
    "call_args : block_arg",
    "$$7 :",
    "command_args : $$7 call_args",
    "block_arg : tAMPER arg_value",
    "opt_block_arg : ',' block_arg",
    "opt_block_arg : none_block_pass",
    "args : arg_value",
    "args : tSTAR arg_value",
    "args : args ',' arg_value",
    "args : args ',' tSTAR arg_value",
    "mrhs_arg : mrhs",
    "mrhs_arg : arg_value",
    "mrhs : args ',' arg_value",
    "mrhs : args ',' tSTAR arg_value",
    "mrhs : tSTAR arg_value",
    "primary : literal",
    "primary : strings",
    "primary : xstring",
    "primary : regexp",
    "primary : words",
    "primary : qwords",
    "primary : symbols",
    "primary : qsymbols",
    "primary : var_ref",
    "primary : backref",
    "primary : tFID",
    "primary : kBEGIN bodystmt kEND",
    "$$8 :",
    "primary : tLPAREN_ARG $$8 rparen",
    "$$9 :",
    "primary : tLPAREN_ARG expr $$9 rparen",
    "primary : tLPAREN compstmt tRPAREN",
    "primary : primary_value tCOLON2 tCONSTANT",
    "primary : tCOLON3 tCONSTANT",
    "primary : tLBRACK aref_args tRBRACK",
    "primary : tLBRACE assoc_list tRCURLY",
    "primary : kRETURN",
    "primary : kYIELD tLPAREN2 call_args rparen",
    "primary : kYIELD tLPAREN2 rparen",
    "primary : kYIELD",
    "primary : kDEFINED opt_nl tLPAREN2 expr rparen",
    "primary : kNOT tLPAREN2 expr rparen",
    "primary : kNOT tLPAREN2 rparen",
    "primary : fcall brace_block",
    "primary : method_call",
    "primary : method_call brace_block",
    "primary : tLAMBDA lambda",
    "primary : kIF expr_value then compstmt if_tail kEND",
    "primary : kUNLESS expr_value then compstmt opt_else kEND",
    "$$10 :",
    "$$11 :",
    "primary : kWHILE $$10 expr_value do $$11 compstmt kEND",
    "$$12 :",
    "$$13 :",
    "primary : kUNTIL $$12 expr_value do $$13 compstmt kEND",
    "primary : kCASE expr_value opt_terms case_body kEND",
    "primary : kCASE opt_terms case_body kEND",
    "$$14 :",
    "$$15 :",
    "primary : kFOR for_var kIN $$14 expr_value do $$15 compstmt kEND",
    "$$16 :",
    "primary : kCLASS cpath superclass $$16 bodystmt kEND",
    "$$17 :",
    "$$18 :",
    "primary : kCLASS tLSHFT expr $$17 term $$18 bodystmt kEND",
    "$$19 :",
    "primary : kMODULE cpath $$19 bodystmt kEND",
    "$$20 :",
    "primary : kDEF fname $$20 f_arglist bodystmt kEND",
    "$$21 :",
    "$$22 :",
    "primary : kDEF singleton dot_or_colon $$21 fname $$22 f_arglist bodystmt kEND",
    "primary : kBREAK",
    "primary : kNEXT",
    "primary : kREDO",
    "primary : kRETRY",
    "primary_value : primary",
    "then : term",
    "then : kTHEN",
    "then : term kTHEN",
    "do : term",
    "do : kDO_COND",
    "if_tail : opt_else",
    "if_tail : kELSIF expr_value then compstmt if_tail",
    "opt_else : none",
    "opt_else : kELSE compstmt",
    "for_var : lhs",
    "for_var : mlhs",
    "f_marg : f_norm_arg",
    "f_marg : tLPAREN f_margs rparen",
    "f_marg_list : f_marg",
    "f_marg_list : f_marg_list ',' f_marg",
    "f_margs : f_marg_list",
    "f_margs : f_marg_list ',' tSTAR f_norm_arg",
    "f_margs : f_marg_list ',' tSTAR f_norm_arg ',' f_marg_list",
    "f_margs : f_marg_list ',' tSTAR",
    "f_margs : f_marg_list ',' tSTAR ',' f_marg_list",
    "f_margs : tSTAR f_norm_arg",
    "f_margs : tSTAR f_norm_arg ',' f_marg_list",
    "f_margs : tSTAR",
    "f_margs : tSTAR ',' f_marg_list",
    "block_args_tail : f_block_kwarg ',' f_kwrest opt_f_block_arg",
    "block_args_tail : f_block_kwarg opt_f_block_arg",
    "block_args_tail : f_kwrest opt_f_block_arg",
    "block_args_tail : f_block_arg",
    "opt_block_args_tail : ',' block_args_tail",
    "opt_block_args_tail :",
    "block_param : f_arg ',' f_block_optarg ',' f_rest_arg opt_block_args_tail",
    "block_param : f_arg ',' f_block_optarg ',' f_rest_arg ',' f_arg opt_block_args_tail",
    "block_param : f_arg ',' f_block_optarg opt_block_args_tail",
    "block_param : f_arg ',' f_block_optarg ',' f_arg opt_block_args_tail",
    "block_param : f_arg ',' f_rest_arg opt_block_args_tail",
    "block_param : f_arg ','",
    "block_param : f_arg ',' f_rest_arg ',' f_arg opt_block_args_tail",
    "block_param : f_arg opt_block_args_tail",
    "block_param : f_block_optarg ',' f_rest_arg opt_block_args_tail",
    "block_param : f_block_optarg ',' f_rest_arg ',' f_arg opt_block_args_tail",
    "block_param : f_block_optarg opt_block_args_tail",
    "block_param : f_block_optarg ',' f_arg opt_block_args_tail",
    "block_param : f_rest_arg opt_block_args_tail",
    "block_param : f_rest_arg ',' f_arg opt_block_args_tail",
    "block_param : block_args_tail",
    "opt_block_param : none",
    "opt_block_param : block_param_def",
    "block_param_def : tPIPE opt_bv_decl tPIPE",
    "block_param_def : tOROP",
    "block_param_def : tPIPE block_param opt_bv_decl tPIPE",
    "opt_bv_decl : opt_nl",
    "opt_bv_decl : opt_nl ';' bv_decls opt_nl",
    "bv_decls : bvar",
    "bv_decls : bv_decls ',' bvar",
    "bvar : tIDENTIFIER",
    "bvar : f_bad_arg",
    "$$23 :",
    "lambda : $$23 f_larglist lambda_body",
    "f_larglist : tLPAREN2 f_args opt_bv_decl tRPAREN",
    "f_larglist : f_args",
    "lambda_body : tLAMBEG compstmt tRCURLY",
    "lambda_body : kDO_LAMBDA compstmt kEND",
    "$$24 :",
    "do_block : kDO_BLOCK $$24 opt_block_param compstmt kEND",
    "block_call : command do_block",
    "block_call : block_call dot_or_colon operation2 opt_paren_args",
    "block_call : block_call dot_or_colon operation2 opt_paren_args brace_block",
    "block_call : block_call dot_or_colon operation2 command_args do_block",
    "method_call : fcall paren_args",
    "method_call : primary_value tDOT operation2 opt_paren_args",
    "method_call : primary_value tCOLON2 operation2 paren_args",
    "method_call : primary_value tCOLON2 operation3",
    "method_call : primary_value tDOT paren_args",
    "method_call : primary_value tCOLON2 paren_args",
    "method_call : kSUPER paren_args",
    "method_call : kSUPER",
    "method_call : primary_value '[' opt_call_args rbracket",
    "$$25 :",
    "brace_block : tLCURLY $$25 opt_block_param compstmt tRCURLY",
    "$$26 :",
    "brace_block : kDO $$26 opt_block_param compstmt kEND",
    "case_body : kWHEN args then compstmt cases",
    "cases : opt_else",
    "cases : case_body",
    "opt_rescue : kRESCUE exc_list exc_var then compstmt opt_rescue",
    "opt_rescue :",
    "exc_list : arg_value",
    "exc_list : mrhs",
    "exc_list : none",
    "exc_var : tASSOC lhs",
    "exc_var : none",
    "opt_ensure : kENSURE compstmt",
    "opt_ensure : none",
    "literal : numeric",
    "literal : symbol",
    "literal : dsym",
    "strings : string",
    "string : tCHAR",
    "string : string1",
    "string : string string1",
    "string1 : tSTRING_BEG string_contents tSTRING_END",
    "xstring : tXSTRING_BEG xstring_contents tSTRING_END",
    "regexp : tREGEXP_BEG regexp_contents tREGEXP_END",
    "words : tWORDS_BEG ' ' tSTRING_END",
    "words : tWORDS_BEG word_list tSTRING_END",
    "word_list :",
    "word_list : word_list word ' '",
    "word : string_content",
    "word : word string_content",
    "symbols : tSYMBOLS_BEG ' ' tSTRING_END",
    "symbols : tSYMBOLS_BEG symbol_list tSTRING_END",
    "symbol_list :",
    "symbol_list : symbol_list word ' '",
    "qwords : tQWORDS_BEG ' ' tSTRING_END",
    "qwords : tQWORDS_BEG qword_list tSTRING_END",
    "qsymbols : tQSYMBOLS_BEG ' ' tSTRING_END",
    "qsymbols : tQSYMBOLS_BEG qsym_list tSTRING_END",
    "qword_list :",
    "qword_list : qword_list tSTRING_CONTENT ' '",
    "qsym_list :",
    "qsym_list : qsym_list tSTRING_CONTENT ' '",
    "string_contents :",
    "string_contents : string_contents string_content",
    "xstring_contents :",
    "xstring_contents : xstring_contents string_content",
    "regexp_contents :",
    "regexp_contents : regexp_contents string_content",
    "string_content : tSTRING_CONTENT",
    "$$27 :",
    "string_content : tSTRING_DVAR $$27 string_dvar",
    "$$28 :",
    "$$29 :",
    "string_content : tSTRING_DBEG $$28 $$29 compstmt tRCURLY",
    "string_dvar : tGVAR",
    "string_dvar : tIVAR",
    "string_dvar : tCVAR",
    "string_dvar : backref",
    "symbol : tSYMBEG sym",
    "sym : fname",
    "sym : tIVAR",
    "sym : tGVAR",
    "sym : tCVAR",
    "dsym : tSYMBEG xstring_contents tSTRING_END",
    "numeric : simple_numeric",
    "numeric : tUMINUS_NUM simple_numeric",
    "simple_numeric : tINTEGER",
    "simple_numeric : tFLOAT",
    "simple_numeric : tRATIONAL",
    "simple_numeric : tIMAGINARY",
    "var_ref : tIDENTIFIER",
    "var_ref : tIVAR",
    "var_ref : tGVAR",
    "var_ref : tCONSTANT",
    "var_ref : tCVAR",
    "var_ref : kNIL",
    "var_ref : kSELF",
    "var_ref : kTRUE",
    "var_ref : kFALSE",
    "var_ref : k__FILE__",
    "var_ref : k__LINE__",
    "var_ref : k__ENCODING__",
    "var_lhs : tIDENTIFIER",
    "var_lhs : tIVAR",
    "var_lhs : tGVAR",
    "var_lhs : tCONSTANT",
    "var_lhs : tCVAR",
    "var_lhs : kNIL",
    "var_lhs : kSELF",
    "var_lhs : kTRUE",
    "var_lhs : kFALSE",
    "var_lhs : k__FILE__",
    "var_lhs : k__LINE__",
    "var_lhs : k__ENCODING__",
    "backref : tNTH_REF",
    "backref : tBACK_REF",
    "superclass : term",
    "$$30 :",
    "superclass : tLT $$30 expr_value term",
    "superclass : error term",
    "f_arglist : tLPAREN2 f_args rparen",
    "f_arglist : f_args term",
    "args_tail : f_kwarg ',' f_kwrest opt_f_block_arg",
    "args_tail : f_kwarg opt_f_block_arg",
    "args_tail : f_kwrest opt_f_block_arg",
    "args_tail : f_block_arg",
    "opt_args_tail : ',' args_tail",
    "opt_args_tail :",
    "f_args : f_arg ',' f_optarg ',' f_rest_arg opt_args_tail",
    "f_args : f_arg ',' f_optarg ',' f_rest_arg ',' f_arg opt_args_tail",
    "f_args : f_arg ',' f_optarg opt_args_tail",
    "f_args : f_arg ',' f_optarg ',' f_arg opt_args_tail",
    "f_args : f_arg ',' f_rest_arg opt_args_tail",
    "f_args : f_arg ',' f_rest_arg ',' f_arg opt_args_tail",
    "f_args : f_arg opt_args_tail",
    "f_args : f_optarg ',' f_rest_arg opt_args_tail",
    "f_args : f_optarg ',' f_rest_arg ',' f_arg opt_args_tail",
    "f_args : f_optarg opt_args_tail",
    "f_args : f_optarg ',' f_arg opt_args_tail",
    "f_args : f_rest_arg opt_args_tail",
    "f_args : f_rest_arg ',' f_arg opt_args_tail",
    "f_args : args_tail",
    "f_args :",
    "f_bad_arg : tCONSTANT",
    "f_bad_arg : tIVAR",
    "f_bad_arg : tGVAR",
    "f_bad_arg : tCVAR",
    "f_norm_arg : f_bad_arg",
    "f_norm_arg : tIDENTIFIER",
    "f_arg_item : f_norm_arg",
    "f_arg_item : tLPAREN f_margs rparen",
    "f_arg : f_arg_item",
    "f_arg : f_arg ',' f_arg_item",
    "f_label : tLABEL",
    "f_kw : f_label arg_value",
    "f_kw : f_label",
    "f_block_kw : f_label primary_value",
    "f_block_kw : f_label",
    "f_block_kwarg : f_block_kw",
    "f_block_kwarg : f_block_kwarg ',' f_block_kw",
    "f_kwarg : f_kw",
    "f_kwarg : f_kwarg ',' f_kw",
    "kwrest_mark : tPOW",
    "kwrest_mark : tDSTAR",
    "f_kwrest : kwrest_mark tIDENTIFIER",
    "f_kwrest : kwrest_mark",
    "f_opt : f_norm_arg '=' arg_value",
    "f_block_opt : tIDENTIFIER '=' primary_value",
    "f_block_optarg : f_block_opt",
    "f_block_optarg : f_block_optarg ',' f_block_opt",
    "f_optarg : f_opt",
    "f_optarg : f_optarg ',' f_opt",
    "restarg_mark : tSTAR2",
    "restarg_mark : tSTAR",
    "f_rest_arg : restarg_mark tIDENTIFIER",
    "f_rest_arg : restarg_mark",
    "blkarg_mark : tAMPER2",
    "blkarg_mark : tAMPER",
    "f_block_arg : blkarg_mark tIDENTIFIER",
    "opt_f_block_arg : ',' f_block_arg",
    "opt_f_block_arg :",
    "singleton : var_ref",
    "$$31 :",
    "singleton : tLPAREN2 $$31 expr rparen",
    "assoc_list : none",
    "assoc_list : assocs trailer",
    "assocs : assoc",
    "assocs : assocs ',' assoc",
    "assoc : arg_value tASSOC arg_value",
    "assoc : tLABEL arg_value",
    "assoc : tSTRING_BEG string_contents tLABEL_END arg_value",
    "assoc : tDSTAR arg_value",
    "operation : tIDENTIFIER",
    "operation : tCONSTANT",
    "operation : tFID",
    "operation2 : tIDENTIFIER",
    "operation2 : tCONSTANT",
    "operation2 : tFID",
    "operation2 : op",
    "operation3 : tIDENTIFIER",
    "operation3 : tFID",
    "operation3 : op",
    "dot_or_colon : tDOT",
    "dot_or_colon : tCOLON2",
    "opt_terms :",
    "opt_terms : terms",
    "opt_nl :",
    "opt_nl : '\\n'",
    "rparen : opt_nl tRPAREN",
    "rbracket : opt_nl tRBRACK",
    "trailer :",
    "trailer : '\\n'",
    "trailer : ','",
    "term : ';'",
    "term : '\\n'",
    "terms : term",
    "terms : terms ';'",
    "none :",
    "none_block_pass :",
    };

  protected org.jruby.parser.YYDebug yydebug;

  /** index-checked interface to {@link #yyNames}.
      @param token single character or <tt>%token</tt> value.
      @return token name or <tt>[illegal]</tt> or <tt>[unknown]</tt>.
    */
  public static String yyName (int token) {
    if (token < 0 || token > yyNames.length) return "[illegal]";
    String name;
    if ((name = yyNames[token]) != null) return name;
    return "[unknown]";
  }


  /** computes list of expected tokens on error by tracing the tables.
      @param state for which to compute the list.
      @return list of token names.
    */
  protected String[] yyExpecting (int state) {
    int token, n, len = 0;
    boolean[] ok = new boolean[yyNames.length];

    if ((n = yySindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }
    if ((n = yyRindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }

    String result[] = new String[len];
    for (n = token = 0; n < len;  ++ token)
      if (ok[token]) result[n++] = yyNames[token];
    return result;
  }

  /** the generated parser, with debugging messages.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @param yydebug debug message writer implementing <tt>yyDebug</tt>, or <tt>null</tt>.
      @return result of the last reduction, if any.
    */
  public Object yyparse (RubyLexer yyLex, Object ayydebug)
				throws java.io.IOException {
    this.yydebug = (org.jruby.parser.YYDebug) ayydebug;
    return yyparse(yyLex);
  }

  /** initial size and increment of the state/value stack [default 256].
      This is not final so that it can be overwritten outside of invocations
      of {@link #yyparse}.
    */
  protected int yyMax;

  /** executed at the beginning of a reduce action.
      Used as <tt>$$ = yyDefault($1)</tt>, prior to the user-specified action, if any.
      Can be overwritten to provide deep copy, etc.
      @param first value for <tt>$1</tt>, or <tt>null</tt>.
      @return first.
    */
  protected Object yyDefault (Object first) {
    return first;
  }

  /** the generated parser.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @return result of the last reduction, if any.
    */
  public Object yyparse (RubyLexer yyLex) throws java.io.IOException {
    if (yyMax <= 0) yyMax = 256;			// initial size
    int yyState = 0, yyStates[] = new int[yyMax];	// state stack
    Object yyVal = null, yyVals[] = new Object[yyMax];	// value stack
    int yyToken = -1;					// current input
    int yyErrorFlag = 0;				// #tokens to shift

    yyLoop: for (int yyTop = 0;; ++ yyTop) {
      if (yyTop >= yyStates.length) {			// dynamically increase
        int[] i = new int[yyStates.length+yyMax];
        System.arraycopy(yyStates, 0, i, 0, yyStates.length);
        yyStates = i;
        Object[] o = new Object[yyVals.length+yyMax];
        System.arraycopy(yyVals, 0, o, 0, yyVals.length);
        yyVals = o;
      }
      yyStates[yyTop] = yyState;
      yyVals[yyTop] = yyVal;
      if (yydebug != null) yydebug.push(yyState, yyVal);

      yyDiscarded: for (;;) {	// discarding a token does not change stack
        int yyN;
        if ((yyN = yyDefRed[yyState]) == 0) {	// else [default] reduce (yyN)
          if (yyToken < 0) {
//            yyToken = yyLex.advance() ? yyLex.token() : 0;
            yyToken = yyLex.nextToken();
            if (yydebug != null)
              yydebug.lex(yyState, yyToken, yyName(yyToken), yyLex.value());
          }
          if ((yyN = yySindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken) {
            if (yydebug != null)
              yydebug.shift(yyState, yyTable[yyN], yyErrorFlag-1);
            yyState = yyTable[yyN];		// shift to yyN
            yyVal = yyLex.value();
            yyToken = -1;
            if (yyErrorFlag > 0) -- yyErrorFlag;
            continue yyLoop;
          }
          if ((yyN = yyRindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken)
            yyN = yyTable[yyN];			// reduce (yyN)
          else
            switch (yyErrorFlag) {
  
            case 0:
              support.yyerror("syntax error", yyExpecting(yyState), yyNames[yyToken]);
              if (yydebug != null) yydebug.error("syntax error");
  
            case 1: case 2:
              yyErrorFlag = 3;
              do {
                if ((yyN = yySindex[yyStates[yyTop]]) != 0
                    && (yyN += yyErrorCode) >= 0 && yyN < yyTable.length
                    && yyCheck[yyN] == yyErrorCode) {
                  if (yydebug != null)
                    yydebug.shift(yyStates[yyTop], yyTable[yyN], 3);
                  yyState = yyTable[yyN];
                  yyVal = yyLex.value();
                  continue yyLoop;
                }
                if (yydebug != null) yydebug.pop(yyStates[yyTop]);
              } while (-- yyTop >= 0);
              if (yydebug != null) yydebug.reject();
              support.yyerror("irrecoverable syntax error");
  
            case 3:
              if (yyToken == 0) {
                if (yydebug != null) yydebug.reject();
                support.yyerror("irrecoverable syntax error at end-of-file");
              }
              if (yydebug != null)
                yydebug.discard(yyState, yyToken, yyName(yyToken),
  							yyLex.value());
              yyToken = -1;
              continue yyDiscarded;		// leave stack alone
            }
        }
        int yyV = yyTop + 1-yyLen[yyN];
        if (yydebug != null)
          yydebug.reduce(yyState, yyStates[yyV-1], yyN, yyRule[yyN], yyLen[yyN]);
        ParserState state = states[yyN];
        if (state == null) {
            yyVal = yyDefault(yyV > yyTop ? null : yyVals[yyV]);
        } else {
            yyVal = state.execute(support, lexer, yyVal, yyVals, yyTop);
        }
//        switch (yyN) {
// ACTIONS_END
//        }
        yyTop -= yyLen[yyN];
        yyState = yyStates[yyTop];
        int yyM = yyLhs[yyN];
        if (yyState == 0 && yyM == 0) {
          if (yydebug != null) yydebug.shift(0, yyFinal);
          yyState = yyFinal;
          if (yyToken < 0) {
            yyToken = yyLex.nextToken();
//            yyToken = yyLex.advance() ? yyLex.token() : 0;
            if (yydebug != null)
               yydebug.lex(yyState, yyToken,yyName(yyToken), yyLex.value());
          }
          if (yyToken == 0) {
            if (yydebug != null) yydebug.accept(yyVal);
            return yyVal;
          }
          continue yyLoop;
        }
        if ((yyN = yyGindex[yyM]) != 0 && (yyN += yyState) >= 0
            && yyN < yyTable.length && yyCheck[yyN] == yyState)
          yyState = yyTable[yyN];
        else
          yyState = yyDgoto[yyM];
        if (yydebug != null) yydebug.shift(yyStates[yyTop], yyState);
        continue yyLoop;
      }
    }
  }

static ParserState[] states = new ParserState[634];
static {
states[1] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.setState(LexState.EXPR_BEG);
                  support.initTopLocalVariables();
    return yyVal;
  }
};
states[2] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
  /* ENEBO: Removed !compile_for_eval which probably is to reduce warnings*/
                  if (((Node)yyVals[0+yyTop]) != null) {
                      /* last expression should not be void */
                      if (((Node)yyVals[0+yyTop]) instanceof BlockNode) {
                          support.checkUselessStatement(((BlockNode)yyVals[0+yyTop]).getLast());
                      } else {
                          support.checkUselessStatement(((Node)yyVals[0+yyTop]));
                      }
                  }
                  support.getResult().setAST(support.addRootNode(((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[3] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  if (((Node)yyVals[-1+yyTop]) instanceof BlockNode) {
                      support.checkUselessStatements(((BlockNode)yyVals[-1+yyTop]));
                  }
                  yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[5] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[6] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((Node)yyVals[-2+yyTop]), support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[7] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[9] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("BEGIN in method");
                    }
    return yyVal;
  }
};
states[10] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.getResult().addBeginNode(new PreExe19Node(((ISourcePosition)yyVals[-4+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop])));
                    yyVal = null;
    return yyVal;
  }
};
states[11] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  Node node = ((Node)yyVals[-3+yyTop]);

                  if (((RescueBodyNode)yyVals[-2+yyTop]) != null) {
                      node = new RescueNode(support.getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop]), ((RescueBodyNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
                  } else if (((Node)yyVals[-1+yyTop]) != null) {
                      support.warn(ID.ELSE_WITHOUT_RESCUE, support.getPosition(((Node)yyVals[-3+yyTop])), "else without rescue is useless");
                      node = support.appendToBlock(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
                  }
                  if (((Node)yyVals[0+yyTop]) != null) {
                      if (node == null) node = NilImplicitNode.NIL;
                      node = new EnsureNode(support.getPosition(((Node)yyVals[-3+yyTop])), node, ((Node)yyVals[0+yyTop]));
                  }

                  support.fixpos(node, ((Node)yyVals[-3+yyTop]));
                  yyVal = node;
    return yyVal;
  }
};
states[12] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-1+yyTop]) instanceof BlockNode) {
                        support.checkUselessStatements(((BlockNode)yyVals[-1+yyTop]));
                    }
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[14] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[15] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((Node)yyVals[-2+yyTop]), support.newline_node(((Node)yyVals[0+yyTop]), support.getPosition(((Node)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[16] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[17] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[18] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   support.yyerror("BEGIN is permitted only at toplevel");
    return yyVal;
  }
};
states[19] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BeginNode(((ISourcePosition)yyVals[-4+yyTop]), ((Node)yyVals[-3+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[-3+yyTop]));
    return yyVal;
  }
};
states[20] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[21] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newAlias(((ISourcePosition)yyVals[-3+yyTop]), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[22] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new VAliasNode(((ISourcePosition)yyVals[-2+yyTop]), ((String)yyVals[-1+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[23] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new VAliasNode(((ISourcePosition)yyVals[-2+yyTop]), ((String)yyVals[-1+yyTop]), "$" + ((BackRefNode)yyVals[0+yyTop]).getType());
    return yyVal;
  }
};
states[24] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("can't make alias for the number variables");
    return yyVal;
  }
};
states[25] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[26] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null);
                    support.fixpos(((Node)yyVal), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[27] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), null, ((Node)yyVals[-2+yyTop]));
                    support.fixpos(((Node)yyVal), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[28] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                        yyVal = new WhileNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((BeginNode)yyVals[-2+yyTop]).getBodyNode(), false);
                    } else {
                        yyVal = new WhileNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), true);
                    }
    return yyVal;
  }
};
states[29] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                        yyVal = new UntilNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((BeginNode)yyVals[-2+yyTop]).getBodyNode(), false);
                    } else {
                        yyVal = new UntilNode(support.getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), true);
                    }
    return yyVal;
  }
};
states[30] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[0+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[0+yyTop]);
                    yyVal = new RescueNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(support.getPosition(((Node)yyVals[-2+yyTop])), null, body, null), null);
    return yyVal;
  }
};
states[31] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.warn(ID.END_IN_METHOD, ((ISourcePosition)yyVals[-3+yyTop]), "END in method; use at_exit");
                    }
                    yyVal = new PostExeNode(((ISourcePosition)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[33] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    ((MultipleAsgn19Node)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                    yyVal = ((MultipleAsgn19Node)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[34] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    ISourcePosition pos = ((AssignableNode)yyVals[-2+yyTop]).getPosition();
                    String asgnOp = ((String)yyVals[-1+yyTop]);
                    if (asgnOp.equals("||")) {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                        yyVal = new OpAsgnOrNode(pos, support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
                    } else if (asgnOp.equals("&&")) {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                        yyVal = new OpAsgnAndNode(pos, support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
                    } else {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode)yyVals[-2+yyTop])), asgnOp, ((Node)yyVals[0+yyTop])));
                        ((AssignableNode)yyVals[-2+yyTop]).setPosition(pos);
                        yyVal = ((AssignableNode)yyVals[-2+yyTop]);
                    }
    return yyVal;
  }
};
states[35] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
  /* FIXME: arg_concat logic missing for opt_call_args*/
                    yyVal = support.new_opElementAsgnNode(((Node)yyVals[-5+yyTop]), ((String)yyVals[-1+yyTop]), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[36] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), ((String)yyVals[-2+yyTop]), ((String)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[37] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), ((String)yyVals[-2+yyTop]), ((String)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[38] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("can't make alias for the number variables");
                    yyVal = null;
    return yyVal;
  }
};
states[39] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), ((String)yyVals[-2+yyTop]), ((String)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[40] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[41] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[42] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                    yyVal = ((MultipleAsgn19Node)yyVals[-2+yyTop]);
                    ((MultipleAsgn19Node)yyVals[-2+yyTop]).setPosition(support.getPosition(((MultipleAsgn19Node)yyVals[-2+yyTop])));
    return yyVal;
  }
};
states[44] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[45] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[47] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newAndNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[48] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOrNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[49] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.getConditionNode(((Node)yyVals[0+yyTop])), "!");
    return yyVal;
  }
};
states[50] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.getConditionNode(((Node)yyVals[0+yyTop])), "!");
    return yyVal;
  }
};
states[52] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[56] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((String)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[57] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[58] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(((ISourcePosition)yyVals[-4+yyTop]), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[59] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_fcall(((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[60] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[61] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
                    yyVal = ((FCallNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[62] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((String)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[63] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((String)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
    return yyVal;
  }
};
states[64] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((String)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[65] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((String)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[66] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_super(((ISourcePosition)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[67] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_yield(((ISourcePosition)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[68] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ReturnNode(((ISourcePosition)yyVals[-1+yyTop]), support.ret_args(((Node)yyVals[0+yyTop]), ((ISourcePosition)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[69] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BreakNode(((ISourcePosition)yyVals[-1+yyTop]), support.ret_args(((Node)yyVals[0+yyTop]), ((ISourcePosition)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[70] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new NextNode(((ISourcePosition)yyVals[-1+yyTop]), support.ret_args(((Node)yyVals[0+yyTop]), ((ISourcePosition)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[72] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[73] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((MultipleAsgn19Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[74] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ISourcePosition)yyVals[-2+yyTop]), support.newArrayNode(((ISourcePosition)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop])), null, null);
    return yyVal;
  }
};
states[75] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[76] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]).add(((Node)yyVals[0+yyTop])), null, null);
    return yyVal;
  }
};
states[77] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), (ListNode) null);
    return yyVal;
  }
};
states[78] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[79] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[80] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[81] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((Node)yyVals[0+yyTop]).getPosition(), null, ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[82] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((Node)yyVals[-2+yyTop]).getPosition(), null, ((Node)yyVals[-2+yyTop]), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[83] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = new MultipleAsgn19Node(lexer.getPosition(), null, new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[84] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = new MultipleAsgn19Node(lexer.getPosition(), null, new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[86] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[87] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[88] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[89] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[90] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[91] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignableLabelOrIdentifier(((String)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[92] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new InstAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[93] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new GlobalAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[94] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) support.compile_error("dynamic constant assignment");

                    yyVal = new ConstDeclNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), null, NilImplicitNode.NIL);
    return yyVal;
  }
};
states[95] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ClassVarAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[96] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to nil");
                    yyVal = null;
    return yyVal;
  }
};
states[97] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't change the value of self");
                    yyVal = null;
    return yyVal;
  }
};
states[98] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to true");
                    yyVal = null;
    return yyVal;
  }
};
states[99] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to false");
                    yyVal = null;
    return yyVal;
  }
};
states[100] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __FILE__");
                    yyVal = null;
    return yyVal;
  }
};
states[101] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __LINE__");
                    yyVal = null;
    return yyVal;
  }
};
states[102] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __ENCODING__");
                    yyVal = null;
    return yyVal;
  }
};
states[103] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[104] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[105] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[106] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[107] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    ISourcePosition position = support.getPosition(((Node)yyVals[-2+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop])), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[108] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    ISourcePosition position = lexer.getPosition();

                    yyVal = new ConstDeclNode(position, null, support.new_colon3(position, ((String)yyVals[0+yyTop])), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[109] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[110] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignableLabelOrIdentifier(((String)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[111] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new InstAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[112] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new GlobalAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[113] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) support.compile_error("dynamic constant assignment");

                    yyVal = new ConstDeclNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), null, NilImplicitNode.NIL);
    return yyVal;
  }
};
states[114] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ClassVarAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[115] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to nil");
                    yyVal = null;
    return yyVal;
  }
};
states[116] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't change the value of self");
                    yyVal = null;
    return yyVal;
  }
};
states[117] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to true");
                    yyVal = null;
    return yyVal;
  }
};
states[118] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to false");
                    yyVal = null;
    return yyVal;
  }
};
states[119] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __FILE__");
                    yyVal = null;
    return yyVal;
  }
};
states[120] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __LINE__");
                    yyVal = null;
    return yyVal;
  }
};
states[121] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __ENCODING__");
                    yyVal = null;
    return yyVal;
  }
};
states[122] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[123] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[124] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[125] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.attrset(((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[126] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    ISourcePosition position = support.getPosition(((Node)yyVals[-2+yyTop]));

                    yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop])), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[127] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("dynamic constant assignment");
                    }

                    ISourcePosition position = lexer.getPosition();

                    yyVal = new ConstDeclNode(position, null, support.new_colon3(position, ((String)yyVals[0+yyTop])), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[128] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[129] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("class/module name must be CONSTANT");
    return yyVal;
  }
};
states[131] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon3(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[132] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(lexer.getPosition(), null, ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[133] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[137] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_ENDFN);
                   yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[138] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_ENDFN);
                   yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[139] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LiteralNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[140] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LiteralNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[141] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((LiteralNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[142] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[143] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newUndef(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[144] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[145] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((Node)yyVals[-3+yyTop]), support.newUndef(((Node)yyVals[-3+yyTop]).getPosition(), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[176] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "__LINE__";
    return yyVal;
  }
};
states[177] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "__FILE__";
    return yyVal;
  }
};
states[178] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "__ENCODING__";
    return yyVal;
  }
};
states[179] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "BEGIN";
    return yyVal;
  }
};
states[180] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "END";
    return yyVal;
  }
};
states[181] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "alias";
    return yyVal;
  }
};
states[182] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "and";
    return yyVal;
  }
};
states[183] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "begin";
    return yyVal;
  }
};
states[184] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "break";
    return yyVal;
  }
};
states[185] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "case";
    return yyVal;
  }
};
states[186] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "class";
    return yyVal;
  }
};
states[187] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "def";
    return yyVal;
  }
};
states[188] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "defined?";
    return yyVal;
  }
};
states[189] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "do";
    return yyVal;
  }
};
states[190] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "else";
    return yyVal;
  }
};
states[191] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "elsif";
    return yyVal;
  }
};
states[192] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "end";
    return yyVal;
  }
};
states[193] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "ensure";
    return yyVal;
  }
};
states[194] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "false";
    return yyVal;
  }
};
states[195] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "for";
    return yyVal;
  }
};
states[196] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "in";
    return yyVal;
  }
};
states[197] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "module";
    return yyVal;
  }
};
states[198] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "next";
    return yyVal;
  }
};
states[199] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "nil";
    return yyVal;
  }
};
states[200] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "not";
    return yyVal;
  }
};
states[201] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "or";
    return yyVal;
  }
};
states[202] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "redo";
    return yyVal;
  }
};
states[203] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "rescue";
    return yyVal;
  }
};
states[204] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "retry";
    return yyVal;
  }
};
states[205] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "return";
    return yyVal;
  }
};
states[206] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "self";
    return yyVal;
  }
};
states[207] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "super";
    return yyVal;
  }
};
states[208] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "then";
    return yyVal;
  }
};
states[209] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "true";
    return yyVal;
  }
};
states[210] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "undef";
    return yyVal;
  }
};
states[211] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "when";
    return yyVal;
  }
};
states[212] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "yield";
    return yyVal;
  }
};
states[213] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "if";
    return yyVal;
  }
};
states[214] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "unless";
    return yyVal;
  }
};
states[215] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "while";
    return yyVal;
  }
};
states[216] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "until";
    return yyVal;
  }
};
states[217] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = "rescue";
    return yyVal;
  }
};
states[218] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    /* FIXME: Consider fixing node_assign itself rather than single case*/
                    ((Node)yyVal).setPosition(support.getPosition(((Node)yyVals[-2+yyTop])));
    return yyVal;
  }
};
states[219] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ISourcePosition position = support.getPosition(((Node)yyVals[-4+yyTop]));
                    Node body = ((Node)yyVals[0+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[0+yyTop]);
                    yyVal = support.node_assign(((Node)yyVals[-4+yyTop]), new RescueNode(position, ((Node)yyVals[-2+yyTop]), new RescueBodyNode(position, null, body, null), null));
    return yyVal;
  }
};
states[220] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    ISourcePosition pos = ((AssignableNode)yyVals[-2+yyTop]).getPosition();
                    String asgnOp = ((String)yyVals[-1+yyTop]);
                    if (asgnOp.equals("||")) {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                        yyVal = new OpAsgnOrNode(pos, support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
                    } else if (asgnOp.equals("&&")) {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                        yyVal = new OpAsgnAndNode(pos, support.gettable2(((AssignableNode)yyVals[-2+yyTop])), ((AssignableNode)yyVals[-2+yyTop]));
                    } else {
                        ((AssignableNode)yyVals[-2+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode)yyVals[-2+yyTop])), asgnOp, ((Node)yyVals[0+yyTop])));
                        ((AssignableNode)yyVals[-2+yyTop]).setPosition(pos);
                        yyVal = ((AssignableNode)yyVals[-2+yyTop]);
                    }
    return yyVal;
  }
};
states[221] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    ISourcePosition pos = support.getPosition(((Node)yyVals[0+yyTop]));
                    Node body = ((Node)yyVals[0+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[0+yyTop]);
                    Node rescue = new RescueNode(pos, ((Node)yyVals[-2+yyTop]), new RescueBodyNode(support.getPosition(((Node)yyVals[-2+yyTop])), null, body, null), null);

                    pos = ((AssignableNode)yyVals[-4+yyTop]).getPosition();
                    String asgnOp = ((String)yyVals[-3+yyTop]);
                    if (asgnOp.equals("||")) {
                        ((AssignableNode)yyVals[-4+yyTop]).setValueNode(rescue);
                        yyVal = new OpAsgnOrNode(pos, support.gettable2(((AssignableNode)yyVals[-4+yyTop])), ((AssignableNode)yyVals[-4+yyTop]));
                    } else if (asgnOp.equals("&&")) {
                        ((AssignableNode)yyVals[-4+yyTop]).setValueNode(rescue);
                        yyVal = new OpAsgnAndNode(pos, support.gettable2(((AssignableNode)yyVals[-4+yyTop])), ((AssignableNode)yyVals[-4+yyTop]));
                    } else {
                        ((AssignableNode)yyVals[-4+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode)yyVals[-4+yyTop])), asgnOp, rescue));
                        ((AssignableNode)yyVals[-4+yyTop]).setPosition(pos);
                        yyVal = ((AssignableNode)yyVals[-4+yyTop]);
                    }
    return yyVal;
  }
};
states[222] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
  /* FIXME: arg_concat missing for opt_call_args*/
                    yyVal = support.new_opElementAsgnNode(((Node)yyVals[-5+yyTop]), ((String)yyVals[-1+yyTop]), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[223] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), ((String)yyVals[-2+yyTop]), ((String)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[224] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), ((String)yyVals[-2+yyTop]), ((String)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[225] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new OpAsgnNode(support.getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), ((String)yyVals[-2+yyTop]), ((String)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[226] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("constant re-assignment");
    return yyVal;
  }
};
states[227] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("constant re-assignment");
    return yyVal;
  }
};
states[228] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.backrefAssignError(((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[229] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));
    
                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), false, isLiteral);
    return yyVal;
  }
};
states[230] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[-2+yyTop]));
                    support.checkExpression(((Node)yyVals[0+yyTop]));

                    boolean isLiteral = ((Node)yyVals[-2+yyTop]) instanceof FixnumNode && ((Node)yyVals[0+yyTop]) instanceof FixnumNode;
                    yyVal = new DotNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), true, isLiteral);
    return yyVal;
  }
};
states[231] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "+", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[232] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "-", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[233] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "*", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[234] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "/", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[235] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "%", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[236] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[237] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), lexer.getPosition()), "-@");
    return yyVal;
  }
};
states[238] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "+@");
    return yyVal;
  }
};
states[239] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "-@");
    return yyVal;
  }
};
states[240] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "|", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[241] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "^", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[242] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "&", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[243] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=>", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[244] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[245] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[246] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[247] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[248] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "==", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[249] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "===", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[250] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "!=", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[251] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getMatchNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                  /* ENEBO
                        $$ = match_op($1, $3);
                        if (nd_type($1) == NODE_LIT && TYPE($1->nd_lit) == T_REGEXP) {
                            $$ = reg_named_capture_assign($1->nd_lit, $$);
                        }
                  */
    return yyVal;
  }
};
states[252] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "!~", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[253] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.getConditionNode(((Node)yyVals[0+yyTop])), "!");
    return yyVal;
  }
};
states[254] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "~");
    return yyVal;
  }
};
states[255] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<<", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[256] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">>", ((Node)yyVals[0+yyTop]), lexer.getPosition());
    return yyVal;
  }
};
states[257] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newAndNode(((Node)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[258] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newOrNode(((Node)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[259] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_defined(((ISourcePosition)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[260] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(support.getPosition(((Node)yyVals[-5+yyTop])), support.getConditionNode(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[261] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[262] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]) != null ? ((Node)yyVals[0+yyTop]) : NilImplicitNode.NIL;
    return yyVal;
  }
};
states[264] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[265] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), ((HashNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[266] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((HashNode)yyVals[-1+yyTop]).getPosition(), ((HashNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[267] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    if (yyVal != null) ((Node)yyVal).setPosition(((ISourcePosition)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[272] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[273] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), ((HashNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[274] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((HashNode)yyVals[-1+yyTop]).getPosition(), ((HashNode)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[275] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[276] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_blk_pass(((Node)yyVals[-1+yyTop]), ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[277] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((HashNode)yyVals[-1+yyTop]).getPosition(), ((HashNode)yyVals[-1+yyTop]));
                    yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[278] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_append(((Node)yyVals[-3+yyTop]), ((HashNode)yyVals[-1+yyTop]));
                    yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[279] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[280] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Long.valueOf(lexer.getCmdArgumentState().begin());
    return yyVal;
  }
};
states[281] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getCmdArgumentState().reset(((Long)yyVals[-1+yyTop]).longValue());
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[282] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockPassNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[283] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((BlockPassNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[285] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ISourcePosition pos = ((Node)yyVals[0+yyTop]) == null ? lexer.getPosition() : ((Node)yyVals[0+yyTop]).getPosition();
                    yyVal = support.newArrayNode(pos, ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[286] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newSplatNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[287] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node = support.splat_array(((Node)yyVals[-2+yyTop]));

                    if (node != null) {
                        yyVal = support.list_append(node, ((Node)yyVals[0+yyTop]));
                    } else {
                        yyVal = support.arg_append(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    }
    return yyVal;
  }
};
states[288] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node = null;

                    /* FIXME: lose syntactical elements here (and others like this)*/
                    if (((Node)yyVals[0+yyTop]) instanceof ArrayNode &&
                        (node = support.splat_array(((Node)yyVals[-3+yyTop]))) != null) {
                        yyVal = support.list_concat(node, ((Node)yyVals[0+yyTop]));
                    } else {
                        yyVal = support.arg_concat(support.getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
                    }
    return yyVal;
  }
};
states[289] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[290] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[291] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node = support.splat_array(((Node)yyVals[-2+yyTop]));

                    if (node != null) {
                        yyVal = support.list_append(node, ((Node)yyVals[0+yyTop]));
                    } else {
                        yyVal = support.arg_append(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
                    }
    return yyVal;
  }
};
states[292] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node = null;

                    if (((Node)yyVals[0+yyTop]) instanceof ArrayNode &&
                        (node = support.splat_array(((Node)yyVals[-3+yyTop]))) != null) {
                        yyVal = support.list_concat(node, ((Node)yyVals[0+yyTop]));
                    } else {
                        yyVal = support.arg_concat(((Node)yyVals[-3+yyTop]).getPosition(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
                    }
    return yyVal;
  }
};
states[293] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.newSplatNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[300] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[0+yyTop]); /* FIXME: Why complaining without $$ = $1;*/
    return yyVal;
  }
};
states[301] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[0+yyTop]); /* FIXME: Why complaining without $$ = $1;*/
    return yyVal;
  }
};
states[304] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_fcall(((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[305] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BeginNode(((ISourcePosition)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[306] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_ENDARG);
    return yyVal;
  }
};
states[307] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null; /*FIXME: Should be implicit nil?*/
    return yyVal;
  }
};
states[308] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_ENDARG); 
    return yyVal;
  }
};
states[309] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (Options.PARSER_WARN_GROUPED_EXPRESSIONS.load()) {
                      support.warning(ID.GROUPED_EXPRESSION, ((ISourcePosition)yyVals[-3+yyTop]), "(...) interpreted as grouped expression");
                    }
                    yyVal = ((Node)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[310] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-1+yyTop]) != null) {
                        /* compstmt position includes both parens around it*/
                        ((ISourcePositionHolder) ((Node)yyVals[-1+yyTop])).setPosition(((ISourcePosition)yyVals[-2+yyTop]));
                        yyVal = ((Node)yyVals[-1+yyTop]);
                    } else {
                        yyVal = new NilNode(((ISourcePosition)yyVals[-2+yyTop]));
                    }
    return yyVal;
  }
};
states[311] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon2(support.getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[312] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_colon3(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[313] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ISourcePosition position = support.getPosition(((Node)yyVals[-1+yyTop]));
                    if (((Node)yyVals[-1+yyTop]) == null) {
                        yyVal = new ZArrayNode(position); /* zero length array */
                    } else {
                        yyVal = ((Node)yyVals[-1+yyTop]);
                    }
    return yyVal;
  }
};
states[314] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((HashNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[315] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ReturnNode(((ISourcePosition)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[316] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_yield(((ISourcePosition)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[317] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZYieldNode(((ISourcePosition)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[318] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZYieldNode(((ISourcePosition)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[319] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_defined(((ISourcePosition)yyVals[-4+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[320] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(support.getConditionNode(((Node)yyVals[-1+yyTop])), "!");
    return yyVal;
  }
};
states[321] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.getOperatorCallNode(NilImplicitNode.NIL, "!");
    return yyVal;
  }
};
states[322] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), null, ((IterNode)yyVals[0+yyTop]));
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);                    
    return yyVal;
  }
};
states[324] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-1+yyTop]) != null && 
                          ((BlockAcceptingNode)yyVals[-1+yyTop]).getIterNode() instanceof BlockPassNode) {
                        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, ((Node)yyVals[-1+yyTop]).getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
                    }
                    yyVal = ((BlockAcceptingNode)yyVals[-1+yyTop]).setIterNode(((IterNode)yyVals[0+yyTop]));
                    ((Node)yyVal).setPosition(((Node)yyVals[-1+yyTop]).getPosition());
    return yyVal;
  }
};
states[325] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((LambdaNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[326] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(((ISourcePosition)yyVals[-5+yyTop]), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[327] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(((ISourcePosition)yyVals[-5+yyTop]), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[328] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().begin();
    return yyVal;
  }
};
states[329] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().end();
    return yyVal;
  }
};
states[330] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[-1+yyTop]);
                    yyVal = new WhileNode(((ISourcePosition)yyVals[-6+yyTop]), support.getConditionNode(((Node)yyVals[-4+yyTop])), body);
    return yyVal;
  }
};
states[331] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().begin();
    return yyVal;
  }
};
states[332] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  lexer.getConditionState().end();
    return yyVal;
  }
};
states[333] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[-1+yyTop]);
                    yyVal = new UntilNode(((ISourcePosition)yyVals[-6+yyTop]), support.getConditionNode(((Node)yyVals[-4+yyTop])), body);
    return yyVal;
  }
};
states[334] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newCaseNode(((ISourcePosition)yyVals[-4+yyTop]), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[335] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newCaseNode(((ISourcePosition)yyVals[-3+yyTop]), null, ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[336] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().begin();
    return yyVal;
  }
};
states[337] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.getConditionState().end();
    return yyVal;
  }
};
states[338] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      /* ENEBO: Lots of optz in 1.9 parser here*/
                    yyVal = new ForNode(((ISourcePosition)yyVals[-8+yyTop]), ((Node)yyVals[-7+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-4+yyTop]), support.getCurrentScope());
    return yyVal;
  }
};
states[339] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) {
                        support.yyerror("class definition in method body");
                    }
                    support.pushLocalScope();
    return yyVal;
  }
};
states[340] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[-1+yyTop]);

                    yyVal = new ClassNode(((ISourcePosition)yyVals[-5+yyTop]), ((Colon3Node)yyVals[-4+yyTop]), support.getCurrentScope(), body, ((Node)yyVals[-3+yyTop]));
                    support.popCurrentScope();
    return yyVal;
  }
};
states[341] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Boolean.valueOf(support.isInDef());
                    support.setInDef(false);
    return yyVal;
  }
};
states[342] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = Integer.valueOf(support.getInSingle());
                    support.setInSingle(0);
                    support.pushLocalScope();
    return yyVal;
  }
};
states[343] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[-1+yyTop]);

                    yyVal = new SClassNode(((ISourcePosition)yyVals[-7+yyTop]), ((Node)yyVals[-5+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(((Boolean)yyVals[-4+yyTop]).booleanValue());
                    support.setInSingle(((Integer)yyVals[-2+yyTop]).intValue());
    return yyVal;
  }
};
states[344] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) { 
                        support.yyerror("module definition in method body");
                    }
                    support.pushLocalScope();
    return yyVal;
  }
};
states[345] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]) == null ? NilImplicitNode.NIL : ((Node)yyVals[-1+yyTop]);

                    yyVal = new ModuleNode(((ISourcePosition)yyVals[-4+yyTop]), ((Colon3Node)yyVals[-3+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
    return yyVal;
  }
};
states[346] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.setInDef(true);
                    support.pushLocalScope();
    return yyVal;
  }
};
states[347] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);
                    if (body == null) body = NilImplicitNode.NIL;

                    yyVal = new DefnNode(((ISourcePosition)yyVals[-5+yyTop]), new ArgumentNode(((ISourcePosition)yyVals[-5+yyTop]), ((String)yyVals[-4+yyTop])), ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInDef(false);
    return yyVal;
  }
};
states[348] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_FNAME);
    return yyVal;
  }
};
states[349] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.setInSingle(support.getInSingle() + 1);
                    support.pushLocalScope();
                    lexer.setState(LexState.EXPR_ENDFN); /* force for args */
    return yyVal;
  }
};
states[350] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node body = ((Node)yyVals[-1+yyTop]);
                    if (body == null) body = NilImplicitNode.NIL;

                    yyVal = new DefsNode(((ISourcePosition)yyVals[-8+yyTop]), ((Node)yyVals[-7+yyTop]), new ArgumentNode(((ISourcePosition)yyVals[-8+yyTop]), ((String)yyVals[-4+yyTop])), ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), body);
                    support.popCurrentScope();
                    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
  }
};
states[351] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BreakNode(((ISourcePosition)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[352] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new NextNode(((ISourcePosition)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[353] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new RedoNode(((ISourcePosition)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[354] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new RetryNode(((ISourcePosition)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[355] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.checkExpression(((Node)yyVals[0+yyTop]));
                    yyVal = ((Node)yyVals[0+yyTop]);
                    if (yyVal == null) yyVal = NilImplicitNode.NIL;
    return yyVal;
  }
};
states[362] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IfNode(((ISourcePosition)yyVals[-4+yyTop]), support.getConditionNode(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[364] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[366] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    return yyVal;
  }
};
states[367] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.assignableLabelOrIdentifier(((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[368] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[369] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[370] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[371] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[0+yyTop]).getPosition(), ((ListNode)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[372] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), support.assignableLabelOrIdentifier(((String)yyVals[0+yyTop]), null), null);
    return yyVal;
  }
};
states[373] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), support.assignableLabelOrIdentifier(((String)yyVals[-2+yyTop]), null), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[374] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-2+yyTop]).getPosition(), ((ListNode)yyVals[-2+yyTop]), new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[375] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(((ListNode)yyVals[-4+yyTop]).getPosition(), ((ListNode)yyVals[-4+yyTop]), new StarNode(lexer.getPosition()), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[376] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(lexer.getPosition(), null, support.assignableLabelOrIdentifier(((String)yyVals[0+yyTop]), null), null);
    return yyVal;
  }
};
states[377] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(lexer.getPosition(), null, support.assignableLabelOrIdentifier(((String)yyVals[-2+yyTop]), null), ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[378] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(lexer.getPosition(), null, new StarNode(lexer.getPosition()), null);
    return yyVal;
  }
};
states[379] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new MultipleAsgn19Node(support.getPosition(((ListNode)yyVals[0+yyTop])), null, null, ((ListNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[380] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), ((String)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[381] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[382] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(lexer.getPosition(), null, ((String)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[383] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((BlockArgNode)yyVals[0+yyTop]).getPosition(), null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[384] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsTailHolder)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[385] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(lexer.getPosition(), null, null, null);
    return yyVal;
  }
};
states[386] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[387] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-7+yyTop]).getPosition(), ((ListNode)yyVals[-7+yyTop]), ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[388] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[389] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[390] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[391] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    RestArgNode rest = new UnnamedRestArgNode(((ListNode)yyVals[-1+yyTop]).getPosition(), null, support.getCurrentScope().addVariable("*"));
                    yyVal = support.new_args(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, rest, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[392] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[393] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[394] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.getPosition(((ListNode)yyVals[-3+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[395] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.getPosition(((ListNode)yyVals[-5+yyTop])), null, ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[396] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(support.getPosition(((ListNode)yyVals[-1+yyTop])), null, ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[397] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-3+yyTop]).getPosition(), null, ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[398] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((RestArgNode)yyVals[-1+yyTop]).getPosition(), null, null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[399] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((RestArgNode)yyVals[-3+yyTop]).getPosition(), null, null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[400] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ArgsTailHolder)yyVals[0+yyTop]).getPosition(), null, null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[401] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* was $$ = null;*/
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[402] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.commandStart = true;
                    yyVal = ((ArgsNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[403] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[404] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[405] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[406] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[407] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[408] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[409] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[410] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.new_bv(((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[411] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[412] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
                    yyVal = lexer.getLeftParenBegin();
                    lexer.setLeftParenBegin(lexer.incrementParenNest());
    return yyVal;
  }
};
states[413] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new LambdaNode(((ArgsNode)yyVals[-1+yyTop]).getPosition(), ((ArgsNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
                    lexer.setLeftParenBegin(((Integer)yyVals[-2+yyTop]));
    return yyVal;
  }
};
states[414] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[415] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[416] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[417] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[418] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[419] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(((ISourcePosition)yyVals[-4+yyTop]), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[420] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    /* Workaround for JRUBY-2326 (MRI does not enter this production for some reason)*/
                    if (((Node)yyVals[-1+yyTop]) instanceof YieldNode) {
                        throw new SyntaxException(PID.BLOCK_GIVEN_TO_YIELD, ((Node)yyVals[-1+yyTop]).getPosition(), lexer.getCurrentLine(), "block given to yield");
                    }
                    if (((Node)yyVals[-1+yyTop]) instanceof BlockAcceptingNode && ((BlockAcceptingNode)yyVals[-1+yyTop]).getIterNode() instanceof BlockPassNode) {
                        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, ((Node)yyVals[-1+yyTop]).getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
                    }
                    if (((Node)yyVals[-1+yyTop]) instanceof NonLocalControlFlowNode) {
                        ((BlockAcceptingNode) ((NonLocalControlFlowNode)yyVals[-1+yyTop]).getValueNode()).setIterNode(((IterNode)yyVals[0+yyTop]));
                    } else {
                        ((BlockAcceptingNode)yyVals[-1+yyTop]).setIterNode(((IterNode)yyVals[0+yyTop]));
                    }
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((Node)yyVal).setPosition(((Node)yyVals[-1+yyTop]).getPosition());
    return yyVal;
  }
};
states[421] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((String)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[422] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((String)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[423] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((String)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[424] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.frobnicate_fcall_args(((FCallNode)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
                    yyVal = ((FCallNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[425] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((String)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[426] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((String)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[427] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]), null, null);
    return yyVal;
  }
};
states[428] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), "call", ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[429] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_call(((Node)yyVals[-2+yyTop]), "call", ((Node)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[430] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_super(((ISourcePosition)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[431] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZSuperNode(((ISourcePosition)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[432] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-3+yyTop]) instanceof SelfNode) {
                        yyVal = support.new_fcall("[]");
                        support.frobnicate_fcall_args(((FCallNode)yyVal), ((Node)yyVals[-1+yyTop]), null);
                    } else {
                        yyVal = support.new_call(((Node)yyVals[-3+yyTop]), "[]", ((Node)yyVals[-1+yyTop]), null);
                    }
    return yyVal;
  }
};
states[433] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[434] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(((ISourcePosition)yyVals[-4+yyTop]), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[435] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.pushBlockScope();
    return yyVal;
  }
};
states[436] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new IterNode(((ISourcePosition)yyVals[-4+yyTop]), ((ArgsNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), support.getCurrentScope());
                    support.popCurrentScope();
    return yyVal;
  }
};
states[437] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newWhenNode(((ISourcePosition)yyVals[-4+yyTop]), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[440] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    Node node;
                    if (((Node)yyVals[-3+yyTop]) != null) {
                        node = support.appendToBlock(support.node_assign(((Node)yyVals[-3+yyTop]), new GlobalVarNode(((ISourcePosition)yyVals[-5+yyTop]), "$!")), ((Node)yyVals[-1+yyTop]));
                        if (((Node)yyVals[-1+yyTop]) != null) {
                            node.setPosition(((ISourcePosition)yyVals[-5+yyTop]));
                        }
                    } else {
                        node = ((Node)yyVals[-1+yyTop]);
                    }
                    Node body = node == null ? NilImplicitNode.NIL : node;
                    yyVal = new RescueBodyNode(((ISourcePosition)yyVals[-5+yyTop]), ((Node)yyVals[-4+yyTop]), body, ((RescueBodyNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[441] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null; 
    return yyVal;
  }
};
states[442] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[443] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.splat_array(((Node)yyVals[0+yyTop]));
                    if (yyVal == null) yyVal = ((Node)yyVals[0+yyTop]); /* ArgsCat or ArgsPush*/
    return yyVal;
  }
};
states[445] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[447] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[450] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new SymbolNode(lexer.getPosition(), new ByteList(((String)yyVals[0+yyTop]).getBytes(), lexer.getEncoding()));
    return yyVal;
  }
};
states[452] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]) instanceof EvStrNode ? new DStrNode(((Node)yyVals[0+yyTop]).getPosition(), lexer.getEncoding()).add(((Node)yyVals[0+yyTop])) : ((Node)yyVals[0+yyTop]);
                    /*
                    NODE *node = $1;
                    if (!node) {
                        node = NEW_STR(STR_NEW0());
                    } else {
                        node = evstr2dstr(node);
                    }
                    $$ = node;
                    */
    return yyVal;
  }
};
states[453] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ByteList aChar = ByteList.create(((String)yyVals[0+yyTop]));
                    aChar.setEncoding(lexer.getEncoding());
                    yyVal = lexer.createStrNode(lexer.getPosition(), aChar, 0);
    return yyVal;
  }
};
states[454] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[455] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[456] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[457] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ISourcePosition position = support.getPosition(((Node)yyVals[-1+yyTop]));

                    if (((Node)yyVals[-1+yyTop]) == null) {
                        yyVal = new XStrNode(position, null);
                    } else if (((Node)yyVals[-1+yyTop]) instanceof StrNode) {
                        yyVal = new XStrNode(position, (ByteList) ((StrNode)yyVals[-1+yyTop]).getValue().clone());
                    } else if (((Node)yyVals[-1+yyTop]) instanceof DStrNode) {
                        yyVal = new DXStrNode(position, ((DStrNode)yyVals[-1+yyTop]));

                        ((Node)yyVal).setPosition(position);
                    } else {
                        yyVal = new DXStrNode(position).add(((Node)yyVals[-1+yyTop]));
                    }
    return yyVal;
  }
};
states[458] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.newRegexpNode(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), (RegexpNode) ((RegexpNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[459] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[460] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[461] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[462] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DStrNode(((ListNode)yyVals[-2+yyTop]).getPosition(), lexer.getEncoding()).add(((Node)yyVals[-1+yyTop])) : ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[463] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[464] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[465] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[466] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[467] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[468] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DSymbolNode(((ListNode)yyVals[-2+yyTop]).getPosition()).add(((Node)yyVals[-1+yyTop])) : support.asSymbol(((ListNode)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[469] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new ZArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[470] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[471] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ZArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[472] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[473] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[474] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[475] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition());
    return yyVal;
  }
};
states[476] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(support.asSymbol(((ListNode)yyVals[-2+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop])));
    return yyVal;
  }
};
states[477] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ByteList aChar = ByteList.create("");
                    aChar.setEncoding(lexer.getEncoding());
                    yyVal = lexer.createStrNode(lexer.getPosition(), aChar, 0);
    return yyVal;
  }
};
states[478] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[479] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[480] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[481] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[482] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* FIXME: mri is different here.*/
                    yyVal = support.literal_concat(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[483] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[484] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = lexer.getStrTerm();
                    lexer.setStrTerm(null);
                    lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[485] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setStrTerm(((StrTerm)yyVals[-1+yyTop]));
                    yyVal = new EvStrNode(support.getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[486] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = lexer.getStrTerm();
                   lexer.setStrTerm(null);
                   lexer.getConditionState().stop();
                   lexer.getCmdArgumentState().stop();
    return yyVal;
  }
};
states[487] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = lexer.getState();
                   lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[488] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.getConditionState().restart();
                   lexer.getCmdArgumentState().restart();
                   lexer.setStrTerm(((StrTerm)yyVals[-3+yyTop]));
                   lexer.setState(((LexState)yyVals[-2+yyTop]));

                   yyVal = support.newEvStrNode(support.getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]));
    return yyVal;
  }
};
states[489] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new GlobalVarNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[490] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new InstVarNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[491] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = new ClassVarNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[493] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     lexer.setState(LexState.EXPR_END);
                     yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[498] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     lexer.setState(LexState.EXPR_END);

                     /* DStrNode: :"some text #{some expression}"*/
                     /* StrNode: :"some text"*/
                     /* EvStrNode :"#{some expression}"*/
                     /* Ruby 1.9 allows empty strings as symbols*/
                     if (((Node)yyVals[-1+yyTop]) == null) {
                         yyVal = new SymbolNode(lexer.getPosition(), new ByteList(new byte[0], lexer.getEncoding()));
                     } else if (((Node)yyVals[-1+yyTop]) instanceof DStrNode) {
                         yyVal = new DSymbolNode(((Node)yyVals[-1+yyTop]).getPosition(), ((DStrNode)yyVals[-1+yyTop]));
                     } else if (((Node)yyVals[-1+yyTop]) instanceof StrNode) {
                         yyVal = new SymbolNode(((Node)yyVals[-1+yyTop]).getPosition(), ((StrNode)yyVals[-1+yyTop]).getValue());
                     } else {
                         yyVal = new DSymbolNode(((Node)yyVals[-1+yyTop]).getPosition());
                         ((DSymbolNode)yyVal).add(((Node)yyVals[-1+yyTop]));
                     }
    return yyVal;
  }
};
states[499] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);  
    return yyVal;
  }
};
states[500] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = support.negateNumeric(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[501] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[502] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((FloatNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[503] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((RationalNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[504] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                     yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[505] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.declareIdentifier(((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[506] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new InstVarNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[507] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new GlobalVarNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[508] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ConstNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[509] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ClassVarNode(lexer.getPosition(), ((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[510] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new NilNode(lexer.getPosition());
    return yyVal;
  }
};
states[511] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new SelfNode(lexer.getPosition());
    return yyVal;
  }
};
states[512] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new TrueNode(lexer.getPosition());
    return yyVal;
  }
};
states[513] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new FalseNode(lexer.getPosition());
    return yyVal;
  }
};
states[514] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new FileNode(lexer.getPosition(), new ByteList(lexer.getPosition().getFile().getBytes(),
                    support.getConfiguration().getRuntime().getEncodingService().getLocaleEncoding()));
    return yyVal;
  }
};
states[515] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new FixnumNode(lexer.getPosition(), lexer.tokline.getLine()+1);
    return yyVal;
  }
};
states[516] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new EncodingNode(lexer.getPosition(), lexer.getEncoding());
    return yyVal;
  }
};
states[517] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.assignableLabelOrIdentifier(((String)yyVals[0+yyTop]), null);
    return yyVal;
  }
};
states[518] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new InstAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[519] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = new GlobalAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[520] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (support.isInDef() || support.isInSingle()) support.compile_error("dynamic constant assignment");

                    yyVal = new ConstDeclNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), null, NilImplicitNode.NIL);
    return yyVal;
  }
};
states[521] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ClassVarAsgnNode(lexer.getPosition(), ((String)yyVals[0+yyTop]), NilImplicitNode.NIL);
    return yyVal;
  }
};
states[522] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to nil");
                    yyVal = null;
    return yyVal;
  }
};
states[523] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't change the value of self");
                    yyVal = null;
    return yyVal;
  }
};
states[524] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to true");
                    yyVal = null;
    return yyVal;
  }
};
states[525] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to false");
                    yyVal = null;
    return yyVal;
  }
};
states[526] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __FILE__");
                    yyVal = null;
    return yyVal;
  }
};
states[527] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __LINE__");
                    yyVal = null;
    return yyVal;
  }
};
states[528] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.compile_error("Can't assign to __ENCODING__");
                    yyVal = null;
    return yyVal;
  }
};
states[529] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[530] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[531] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[532] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[533] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[534] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                   yyVal = null;
    return yyVal;
  }
};
states[535] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-1+yyTop]);
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = true;
    return yyVal;
  }
};
states[536] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsNode)yyVals[-1+yyTop]);
                    lexer.setState(LexState.EXPR_BEG);
                    lexer.commandStart = true;
    return yyVal;
  }
};
states[537] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), ((String)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[538] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[539] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(lexer.getPosition(), null, ((String)yyVals[-1+yyTop]), ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[540] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(((BlockArgNode)yyVals[0+yyTop]).getPosition(), null, null, ((BlockArgNode)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[541] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ArgsTailHolder)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[542] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args_tail(lexer.getPosition(), null, null, null);
    return yyVal;
  }
};
states[543] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[544] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-7+yyTop]).getPosition(), ((ListNode)yyVals[-7+yyTop]), ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[545] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[546] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[547] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-3+yyTop]).getPosition(), ((ListNode)yyVals[-3+yyTop]), null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[548] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-5+yyTop]).getPosition(), ((ListNode)yyVals[-5+yyTop]), null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[549] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[550] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-3+yyTop]).getPosition(), null, ((ListNode)yyVals[-3+yyTop]), ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[551] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-5+yyTop]).getPosition(), null, ((ListNode)yyVals[-5+yyTop]), ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[552] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-1+yyTop]).getPosition(), null, ((ListNode)yyVals[-1+yyTop]), null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[553] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ListNode)yyVals[-3+yyTop]).getPosition(), null, ((ListNode)yyVals[-3+yyTop]), null, ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[554] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((RestArgNode)yyVals[-1+yyTop]).getPosition(), null, null, ((RestArgNode)yyVals[-1+yyTop]), null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[555] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((RestArgNode)yyVals[-3+yyTop]).getPosition(), null, null, ((RestArgNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[556] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(((ArgsTailHolder)yyVals[0+yyTop]).getPosition(), null, null, null, null, ((ArgsTailHolder)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[557] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.new_args(lexer.getPosition(), null, null, null, null, (ArgsTailHolder) null);
    return yyVal;
  }
};
states[558] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be a constant");
    return yyVal;
  }
};
states[559] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be an instance variable");
    return yyVal;
  }
};
states[560] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be a global variable");
    return yyVal;
  }
};
states[561] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.yyerror("formal argument cannot be a class variable");
    return yyVal;
  }
};
states[563] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.formal_argument(((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[564] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.arg_var(((String)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[565] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    /*            {
            ID tid = internal_id();
            arg_var(tid);
            if (dyna_in_block()) {
                $2->nd_value = NEW_DVAR(tid);
            }
            else {
                $2->nd_value = NEW_LVAR(tid);
            }
            $$ = NEW_ARGS_AUX(tid, 1);
            $$->nd_next = $2;*/
    return yyVal;
  }
};
states[566] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(lexer.getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[567] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
                    yyVal = ((ListNode)yyVals[-2+yyTop]);
    return yyVal;
  }
};
states[568] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((String)yyVals[0+yyTop])));
                    yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[569] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.keyword_arg(((Node)yyVals[0+yyTop]).getPosition(), support.assignableLabelOrIdentifier(((String)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[570] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.keyword_arg(lexer.getPosition(), support.assignableLabelOrIdentifier(((String)yyVals[0+yyTop]), new RequiredKeywordArgumentValueNode()));
    return yyVal;
  }
};
states[571] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.keyword_arg(support.getPosition(((Node)yyVals[0+yyTop])), support.assignableLabelOrIdentifier(((String)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[572] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.keyword_arg(lexer.getPosition(), support.assignableLabelOrIdentifier(((String)yyVals[0+yyTop]), new RequiredKeywordArgumentValueNode()));
    return yyVal;
  }
};
states[573] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[574] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[575] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[576] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[577] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[578] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[579] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.shadowing_lvar(((String)yyVals[0+yyTop]));
                    yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[580] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.internalId();
    return yyVal;
  }
};
states[581] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(((String)yyVals[-2+yyTop]));
                    yyVal = new OptArgNode(support.getPosition(((Node)yyVals[0+yyTop])), support.assignableLabelOrIdentifier(((String)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[582] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    support.arg_var(support.formal_argument(((String)yyVals[-2+yyTop])));
                    yyVal = new OptArgNode(support.getPosition(((Node)yyVals[0+yyTop])), support.assignableLabelOrIdentifier(((String)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
    return yyVal;
  }
};
states[583] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockNode(((Node)yyVals[0+yyTop]).getPosition()).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[584] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[585] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new BlockNode(((Node)yyVals[0+yyTop]).getPosition()).add(((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[586] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[589] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!support.is_local_id(((String)yyVals[0+yyTop]))) {
                        support.yyerror("rest argument must be local variable");
                    }
                    
                    yyVal = new RestArgNode(support.arg_var(support.shadowing_lvar(((String)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[590] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new UnnamedRestArgNode(lexer.getPosition(), "", support.getCurrentScope().addVariable("*"));
    return yyVal;
  }
};
states[593] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!support.is_local_id(((String)yyVals[0+yyTop]))) {
                        support.yyerror("block argument must be local variable");
                    }
                    
                    yyVal = new BlockArgNode(support.arg_var(support.shadowing_lvar(((String)yyVals[0+yyTop]))));
    return yyVal;
  }
};
states[594] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((BlockArgNode)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[595] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = null;
    return yyVal;
  }
};
states[596] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (!(((Node)yyVals[0+yyTop]) instanceof SelfNode)) {
                        support.checkExpression(((Node)yyVals[0+yyTop]));
                    }
                    yyVal = ((Node)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[597] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    lexer.setState(LexState.EXPR_BEG);
    return yyVal;
  }
};
states[598] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-1+yyTop]) == null) {
                        support.yyerror("can't define single method for ().");
                    } else if (((Node)yyVals[-1+yyTop]) instanceof ILiteralNode) {
                        support.yyerror("can't define single method for literals.");
                    }
                    support.checkExpression(((Node)yyVals[-1+yyTop]));
                    yyVal = ((Node)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[599] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new HashNode(lexer.getPosition());
    return yyVal;
  }
};
states[600] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((HashNode)yyVals[-1+yyTop]);
    return yyVal;
  }
};
states[601] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new HashNode(lexer.getPosition(), ((KeyValuePair)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[602] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((HashNode)yyVals[-2+yyTop]).add(((KeyValuePair)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[603] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new KeyValuePair<Node,Node>(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[604] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    SymbolNode label = new SymbolNode(support.getPosition(((Node)yyVals[0+yyTop])), new ByteList(((String)yyVals[-1+yyTop]).getBytes(), lexer.getEncoding()));
                    yyVal = new KeyValuePair<Node,Node>(label, ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[605] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    if (((Node)yyVals[-2+yyTop]) instanceof StrNode) {
                        DStrNode dnode = new DStrNode(support.getPosition(((Node)yyVals[-2+yyTop])), lexer.getEncoding());
                        dnode.add(((Node)yyVals[-2+yyTop]));
                        yyVal = new KeyValuePair<Node,Node>(new DSymbolNode(support.getPosition(((Node)yyVals[-2+yyTop])), dnode), ((Node)yyVals[0+yyTop]));
                    } else if (((Node)yyVals[-2+yyTop]) instanceof DStrNode) {
                        yyVal = new KeyValuePair<Node,Node>(new DSymbolNode(support.getPosition(((Node)yyVals[-2+yyTop])), ((DStrNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop]));
                    } else {
                        support.compile_error("Uknown type for assoc in strings: " + ((Node)yyVals[-2+yyTop]));
                    }

    return yyVal;
  }
};
states[606] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = new KeyValuePair<Node,Node>(null, ((Node)yyVals[0+yyTop]));
    return yyVal;
  }
};
states[623] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[624] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                    yyVal = ((String)yyVals[0+yyTop]);
    return yyVal;
  }
};
states[632] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                      yyVal = null;
    return yyVal;
  }
};
states[633] = new ParserState() {
  @Override public Object execute(ParserSupport support, RubyLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
                  yyVal = null;
    return yyVal;
  }
};
}
					// line 2502 "RubyParser.y"

    /** The parse method use an lexer stream and parse it to an AST node 
     * structure
     */
    public RubyParserResult parse(ParserConfiguration configuration, LexerSource source) throws IOException {
        support.reset();
        support.setConfiguration(configuration);
        support.setResult(new RubyParserResult());
        
        lexer.reset();
        lexer.setSource(source);
        lexer.setEncoding(configuration.getDefaultEncoding());

        yyparse(lexer, configuration.isDebug() ? new YYDebug() : null);
        
        return support.getResult();
    }
}
					// line 9703 "-"
