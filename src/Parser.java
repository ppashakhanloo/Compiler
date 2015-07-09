import java.util.Arrays;
import java.util.Stack;

public class Parser {

	int[][] ActionTable_S = new int[151][35];
	int[][] ActionTable_R = new int[151][35];
	int[][] GotoTable = new int[151][37];

	int[] RHS = new int[65];
	int[] LHS = new int[65];

	LexicalAnalyzer scanner;
	Stack<Integer> parseStack = new Stack<Integer>();

	public static String errorMessage = "";

	public Parser(String filename) throws Exception {
		for (int i = 0; i < 151; i++)
			for (int j = 0; j < 35; j++) {
				ActionTable_S[i][j] = -1;
				ActionTable_R[i][j] = -1;
			}
		for (int i = 0; i < 151; i++)
			for (int j = 0; j < 37; j++) {
				GotoTable[i][j] = -1;
			}

		ActionTable_R[0][12] = 53;

		ActionTable_R[0][13] = 53;
		ActionTable_R[0][14] = 53;
		ActionTable_R[0][22] = 53;
		ActionTable_R[0][24] = 53;
		ActionTable_R[0][25] = 53;
		ActionTable_R[0][32] = 53;

		ActionTable_R[1][34] = 0;

		ActionTable_R[2][12] = 5;
		ActionTable_S[2][13] = 6;

		ActionTable_S[3][11] = 7;

		ActionTable_S[4][12] = 9;

		ActionTable_R[5][12] = 5;
		ActionTable_S[5][13] = 6;

		ActionTable_S[6][14] = 11;

		ActionTable_R[7][34] = 1;

		ActionTable_R[8][11] = 2;

		ActionTable_S[9][13] = 12;

		ActionTable_R[10][12] = 4;

		ActionTable_R[11][1] = 55;
		ActionTable_R[11][7] = 55;
		ActionTable_R[11][14] = 55;
		ActionTable_R[11][18] = 55;
		ActionTable_R[11][32] = 55;

		ActionTable_S[12][14] = 14;

		ActionTable_S[13][18] = 16;
		ActionTable_R[13][32] = 8;

		ActionTable_S[14][32] = 17;

		ActionTable_S[15][32] = 18;

		ActionTable_S[16][14] = 19;

		ActionTable_S[17][12] = 20;

		ActionTable_R[18][12] = 10;
		ActionTable_S[18][15] = 23;
		ActionTable_R[18][33] = 10;

		ActionTable_R[19][32] = 7;

		ActionTable_S[20][15] = 24;

		ActionTable_S[21][12] = 27;
		ActionTable_R[21][33] = 16;

		ActionTable_R[22][12] = 10;
		ActionTable_S[22][15] = 23;
		ActionTable_R[22][33] = 10;

		ActionTable_S[23][20] = 30;
		ActionTable_S[23][21] = 31;

		ActionTable_S[24][16] = 32;

		ActionTable_S[25][33] = 33;

		ActionTable_S[26][12] = 27;
		ActionTable_R[26][33] = 16;

		ActionTable_S[27][15] = 35;

		ActionTable_R[28][12] = 9;
		ActionTable_R[28][33] = 9;

		ActionTable_S[29][14] = 36;

		ActionTable_R[30][14] = 22;

		ActionTable_R[31][14] = 23;

		ActionTable_S[32][17] = 37;

		ActionTable_R[33][12] = 6;
		ActionTable_R[33][13] = 6;

		ActionTable_R[34][33] = 15;

		ActionTable_S[35][20] = 30;
		ActionTable_S[35][21] = 31;

		ActionTable_S[36][8] = 39;

		ActionTable_S[37][1] = 40;

		ActionTable_R[38][1] = 55;
		ActionTable_R[38][7] = 55;
		ActionTable_R[38][14] = 55;
		ActionTable_R[38][18] = 55;
		ActionTable_R[38][32] = 55;

		ActionTable_R[39][12] = 11;
		ActionTable_R[39][15] = 11;
		ActionTable_R[39][33] = 11;

		ActionTable_S[40][2] = 42;

		ActionTable_S[41][14] = 43;

		ActionTable_S[42][32] = 44;

		ActionTable_R[43][1] = 55;
		ActionTable_R[43][7] = 55;
		ActionTable_R[43][14] = 55;
		ActionTable_R[43][18] = 55;
		ActionTable_R[43][32] = 55;

		ActionTable_R[44][14] = 13;
		ActionTable_R[44][19] = 13;
		ActionTable_S[44][20] = 30;
		ActionTable_S[44][21] = 31;
		ActionTable_R[44][22] = 13;
		ActionTable_R[44][24] = 13;
		ActionTable_R[44][25] = 13;
		ActionTable_R[44][32] = 13;
		ActionTable_R[44][33] = 13;

		ActionTable_S[45][1] = 49;

		ActionTable_R[46][14] = 54;
		ActionTable_R[46][22] = 54;
		ActionTable_R[46][24] = 54;
		ActionTable_R[46][25] = 54;
		ActionTable_R[46][32] = 54;
		ActionTable_R[46][33] = 54;

		ActionTable_S[47][14] = 51;

		ActionTable_R[48][14] = 13;
		ActionTable_R[48][19] = 13;
		ActionTable_S[48][20] = 30;
		ActionTable_S[48][21] = 31;
		ActionTable_R[48][22] = 13;
		ActionTable_R[48][24] = 13;
		ActionTable_R[48][25] = 13;
		ActionTable_R[48][32] = 13;
		ActionTable_R[48][33] = 13;

		ActionTable_R[49][2] = 56;
		ActionTable_R[49][20] = 56;
		ActionTable_R[49][21] = 56;

		ActionTable_R[50][14] = 25;
		ActionTable_R[50][19] = 25;
		ActionTable_R[50][22] = 25;
		ActionTable_R[50][24] = 25;
		ActionTable_R[50][25] = 25;
		ActionTable_R[50][32] = 25;
		ActionTable_R[50][33] = 25;

		ActionTable_S[51][8] = 55;

		ActionTable_R[52][14] = 12;
		ActionTable_R[52][19] = 12;
		ActionTable_R[52][22] = 12;
		ActionTable_R[52][24] = 12;
		ActionTable_R[52][25] = 12;
		ActionTable_R[52][32] = 12;
		ActionTable_R[52][33] = 12;

		ActionTable_R[53][2] = 19;
		ActionTable_S[53][20] = 30;
		ActionTable_S[53][21] = 31;

		ActionTable_S[54][14] = 58;
		ActionTable_S[54][22] = 59;
		ActionTable_S[54][24] = 60;
		ActionTable_S[54][25] = 61;
		ActionTable_S[54][32] = 62;
		ActionTable_S[54][33] = 63;

		ActionTable_R[55][14] = 14;
		ActionTable_R[55][19] = 14;
		ActionTable_R[55][20] = 14;
		ActionTable_R[55][21] = 14;
		ActionTable_R[55][22] = 14;
		ActionTable_R[55][24] = 14;
		ActionTable_R[55][25] = 14;
		ActionTable_R[55][32] = 14;
		ActionTable_R[55][33] = 14;

		ActionTable_S[56][14] = 65;

		ActionTable_S[57][2] = 66;

		ActionTable_R[58][10] = 62;

		ActionTable_S[59][1] = 68;

		ActionTable_R[60][1] = 61;

		ActionTable_S[61][7] = 70;

		ActionTable_R[62][14] = 25;
		ActionTable_R[62][19] = 25;
		ActionTable_R[62][22] = 25;
		ActionTable_R[62][24] = 25;
		ActionTable_R[62][25] = 25;
		ActionTable_R[62][32] = 25;
		ActionTable_R[62][33] = 25;

		ActionTable_S[63][33] = 72;

		ActionTable_R[64][14] = 24;
		ActionTable_R[64][19] = 24;
		ActionTable_R[64][22] = 24;
		ActionTable_R[64][24] = 24;
		ActionTable_R[64][25] = 24;
		ActionTable_R[64][32] = 24;
		ActionTable_R[64][33] = 24;

		ActionTable_R[65][2] = 59;
		ActionTable_R[65][5] = 59;

		ActionTable_R[66][32] = 57;

		ActionTable_S[67][10] = 75;

		ActionTable_S[68][1] = 76;
		ActionTable_S[68][14] = 81;
		ActionTable_S[68][28] = 82;
		ActionTable_S[68][29] = 83;
		ActionTable_S[68][30] = 84;

		ActionTable_S[69][1] = 87;

		ActionTable_S[70][26] = 88;

		ActionTable_S[71][14] = 58;
		ActionTable_S[71][22] = 59;
		ActionTable_S[71][24] = 60;
		ActionTable_S[71][25] = 61;
		ActionTable_S[71][32] = 62;
		ActionTable_S[71][33] = 89;

		ActionTable_R[72][11] = 3;

		ActionTable_R[73][2] = 21;
		ActionTable_S[73][5] = 90;

		ActionTable_S[74][32] = 92;

		ActionTable_S[75][1] = 76;
		ActionTable_S[75][14] = 81;
		ActionTable_S[75][28] = 82;
		ActionTable_S[75][29] = 83;
		ActionTable_S[75][30] = 84;

		ActionTable_S[76][1] = 76;
		ActionTable_S[76][14] = 81;
		ActionTable_S[76][28] = 82;
		ActionTable_S[76][29] = 83;
		ActionTable_S[76][30] = 84;

		ActionTable_R[77][2] = 31;
		ActionTable_S[77][4] = 95;
		ActionTable_R[77][5] = 31;
		ActionTable_S[77][6] = 96;
		ActionTable_R[77][8] = 31;
		ActionTable_S[77][9] = 97;
		ActionTable_S[77][31] = 98;

		ActionTable_S[78][0] = 99;
		ActionTable_R[78][2] = 32;
		ActionTable_R[78][5] = 32;
		ActionTable_R[78][8] = 32;

		ActionTable_S[79][2] = 100;

		ActionTable_R[80][0] = 35;
		ActionTable_R[80][2] = 35;
		ActionTable_S[80][3] = 101;
		ActionTable_R[80][4] = 35;
		ActionTable_R[80][5] = 35;
		ActionTable_R[80][6] = 35;
		ActionTable_R[80][8] = 35;
		ActionTable_R[80][9] = 35;
		ActionTable_R[80][31] = 35;

		ActionTable_R[81][0] = 39;
		ActionTable_R[81][1] = 55;
		ActionTable_R[81][2] = 39;
		ActionTable_R[81][3] = 39;
		ActionTable_R[81][4] = 39;
		ActionTable_R[81][5] = 39;
		ActionTable_R[81][6] = 39;
		ActionTable_R[81][7] = 55;
		ActionTable_R[81][8] = 39;
		ActionTable_R[81][9] = 39;
		ActionTable_R[81][14] = 55;
		ActionTable_R[81][18] = 55;
		ActionTable_R[81][31] = 39;
		ActionTable_R[81][32] = 55;

		ActionTable_R[82][0] = 42;
		ActionTable_R[82][2] = 42;
		ActionTable_R[82][3] = 42;
		ActionTable_R[82][4] = 42;
		ActionTable_R[82][5] = 42;
		ActionTable_R[82][6] = 42;
		ActionTable_R[82][8] = 42;
		ActionTable_R[82][9] = 42;
		ActionTable_R[82][31] = 42;

		ActionTable_R[83][0] = 43;
		ActionTable_R[83][2] = 43;
		ActionTable_R[83][3] = 43;
		ActionTable_R[83][4] = 43;
		ActionTable_R[83][5] = 43;
		ActionTable_R[83][6] = 43;
		ActionTable_R[83][8] = 43;
		ActionTable_R[83][9] = 43;
		ActionTable_R[83][31] = 43;

		ActionTable_R[84][0] = 44;
		ActionTable_R[84][2] = 44;
		ActionTable_R[84][3] = 44;
		ActionTable_R[84][4] = 44;
		ActionTable_R[84][5] = 44;
		ActionTable_R[84][6] = 44;
		ActionTable_R[84][8] = 44;
		ActionTable_R[84][9] = 44;
		ActionTable_R[84][31] = 44;

		ActionTable_R[85][0] = 37;
		ActionTable_R[85][2] = 37;
		ActionTable_R[85][3] = 37;
		ActionTable_R[85][4] = 37;
		ActionTable_R[85][5] = 37;
		ActionTable_R[85][6] = 37;
		ActionTable_R[85][8] = 37;
		ActionTable_R[85][9] = 37;
		ActionTable_R[85][31] = 37;

		ActionTable_R[86][0] = 46;
		ActionTable_R[86][2] = 46;
		ActionTable_R[86][5] = 46;
		ActionTable_R[86][8] = 46;

		ActionTable_S[87][1] = 76;
		ActionTable_S[87][14] = 81;
		ActionTable_S[87][28] = 82;
		ActionTable_S[87][29] = 83;
		ActionTable_S[87][30] = 84;

		ActionTable_S[88][7] = 104;

		ActionTable_R[89][14] = 26;
		ActionTable_R[89][19] = 26;
		ActionTable_R[89][22] = 26;
		ActionTable_R[89][23] = 26;
		ActionTable_R[89][24] = 26;
		ActionTable_R[89][25] = 26;
		ActionTable_R[89][32] = 26;
		ActionTable_R[89][33] = 26;

		ActionTable_S[90][20] = 30;
		ActionTable_S[90][21] = 31;

		ActionTable_R[91][2] = 18;

		ActionTable_R[92][14] = 13;
		ActionTable_R[92][19] = 13;
		ActionTable_S[92][20] = 30;
		ActionTable_S[92][21] = 31;
		ActionTable_R[92][22] = 13;
		ActionTable_R[92][24] = 13;
		ActionTable_R[92][25] = 13;
		ActionTable_R[92][32] = 13;
		ActionTable_R[92][33] = 13;

		ActionTable_S[93][8] = 107;

		ActionTable_S[94][2] = 108;
		ActionTable_S[94][4] = 95;
		ActionTable_S[94][6] = 96;

		ActionTable_S[95][1] = 76;
		ActionTable_S[95][14] = 81;
		ActionTable_S[95][28] = 82;
		ActionTable_S[95][29] = 83;
		ActionTable_S[95][30] = 84;

		ActionTable_S[96][1] = 76;
		ActionTable_S[96][14] = 81;
		ActionTable_S[96][28] = 82;
		ActionTable_S[96][29] = 83;
		ActionTable_S[96][30] = 84;

		ActionTable_S[97][1] = 76;
		ActionTable_S[97][14] = 81;
		ActionTable_S[97][28] = 82;
		ActionTable_S[97][29] = 83;
		ActionTable_S[97][30] = 84;

		ActionTable_S[98][1] = 76;
		ActionTable_S[98][14] = 81;
		ActionTable_S[98][28] = 82;
		ActionTable_S[98][29] = 83;
		ActionTable_S[98][30] = 84;

		ActionTable_S[99][1] = 76;
		ActionTable_S[99][14] = 81;
		ActionTable_S[99][28] = 82;
		ActionTable_S[99][29] = 83;
		ActionTable_S[99][30] = 84;

		ActionTable_R[100][12] = 53;
		ActionTable_R[100][13] = 53;
		ActionTable_R[100][14] = 53;
		ActionTable_R[100][22] = 53;
		ActionTable_R[100][24] = 53;
		ActionTable_R[100][25] = 53;
		ActionTable_R[100][32] = 53;

		ActionTable_S[101][1] = 76;
		ActionTable_S[101][14] = 81;
		ActionTable_S[101][28] = 82;
		ActionTable_S[101][29] = 83;
		ActionTable_S[101][30] = 84;

		ActionTable_S[102][7] = 117;

		ActionTable_S[103][2] = 118;

		ActionTable_S[104][27] = 119;

		ActionTable_S[105][14] = 120;

		ActionTable_R[106][14] = 58;
		ActionTable_R[106][19] = 58;
		ActionTable_R[106][22] = 58;
		ActionTable_R[106][24] = 58;
		ActionTable_R[106][25] = 58;
		ActionTable_R[106][32] = 58;

		ActionTable_R[107][14] = 30;
		ActionTable_R[107][19] = 30;
		ActionTable_R[107][22] = 30;
		ActionTable_R[107][23] = 30;
		ActionTable_R[107][24] = 30;
		ActionTable_R[107][25] = 30;
		ActionTable_R[107][32] = 30;
		ActionTable_R[107][33] = 30;

		ActionTable_R[108][0] = 38;
		ActionTable_R[108][2] = 38;
		ActionTable_R[108][3] = 38;
		ActionTable_R[108][4] = 38;
		ActionTable_R[108][5] = 38;
		ActionTable_R[108][6] = 38;
		ActionTable_R[108][8] = 38;
		ActionTable_R[108][9] = 38;
		ActionTable_R[108][31] = 38;

		ActionTable_R[109][0] = 33;
		ActionTable_R[109][2] = 33;
		ActionTable_S[109][3] = 101;
		ActionTable_R[109][4] = 33;
		ActionTable_R[109][5] = 33;
		ActionTable_R[109][6] = 33;
		ActionTable_R[109][8] = 33;
		ActionTable_R[109][9] = 33;
		ActionTable_R[109][31] = 33;

		ActionTable_R[110][0] = 34;
		ActionTable_R[110][2] = 34;
		ActionTable_S[110][3] = 101;
		ActionTable_R[110][4] = 34;
		ActionTable_R[110][5] = 34;
		ActionTable_R[110][6] = 34;
		ActionTable_R[110][8] = 34;
		ActionTable_R[110][9] = 34;
		ActionTable_R[110][31] = 34;

		ActionTable_R[111][0] = 48;
		ActionTable_R[111][2] = 48;
		ActionTable_S[111][4] = 95;
		ActionTable_R[111][5] = 48;
		ActionTable_S[111][6] = 96;
		ActionTable_R[111][8] = 48;

		ActionTable_R[112][0] = 47;
		ActionTable_R[112][2] = 47;
		ActionTable_S[112][4] = 95;
		ActionTable_R[112][5] = 47;
		ActionTable_S[112][6] = 96;
		ActionTable_R[112][8] = 47;

		ActionTable_S[113][4] = 95;
		ActionTable_S[113][6] = 96;
		ActionTable_S[113][9] = 97;
		ActionTable_S[113][31] = 98;

		ActionTable_R[114][0] = 45;
		ActionTable_R[114][2] = 45;
		ActionTable_R[114][5] = 45;
		ActionTable_R[114][8] = 45;

		ActionTable_S[115][14] = 58;
		ActionTable_S[115][22] = 59;
		ActionTable_S[115][24] = 60;
		ActionTable_S[115][25] = 61;
		ActionTable_S[115][32] = 62;

		ActionTable_R[116][0] = 36;
		ActionTable_R[116][2] = 36;
		ActionTable_R[116][3] = 36;
		ActionTable_R[116][4] = 36;
		ActionTable_R[116][5] = 36;
		ActionTable_R[116][6] = 36;
		ActionTable_R[116][8] = 36;
		ActionTable_R[116][9] = 36;
		ActionTable_R[116][31] = 36;

		ActionTable_S[117][14] = 123;

		ActionTable_R[118][12] = 53;
		ActionTable_R[118][13] = 53;
		ActionTable_R[118][14] = 53;
		ActionTable_R[118][22] = 53;
		ActionTable_R[118][24] = 53;
		ActionTable_R[118][25] = 53;
		ActionTable_R[118][32] = 53;

		ActionTable_S[119][1] = 125;

		ActionTable_R[120][2] = 59;
		ActionTable_R[120][5] = 59;

		ActionTable_R[121][14] = 25;
		ActionTable_R[121][19] = 25;
		ActionTable_R[121][22] = 25;
		ActionTable_R[121][24] = 25;
		ActionTable_R[121][25] = 25;
		ActionTable_R[121][32] = 25;
		ActionTable_R[121][33] = 25;

		ActionTable_S[122][23] = 128;

		ActionTable_R[123][0] = 40;
		ActionTable_R[123][1] = 63;
		ActionTable_R[123][2] = 40;
		ActionTable_R[123][3] = 40;
		ActionTable_R[123][4] = 40;
		ActionTable_R[123][5] = 40;
		ActionTable_R[123][6] = 40;
		ActionTable_R[123][8] = 40;
		ActionTable_R[123][9] = 40;
		ActionTable_R[123][31] = 40;

		ActionTable_S[124][14] = 58;
		ActionTable_S[124][22] = 59;
		ActionTable_S[124][24] = 60;
		ActionTable_S[124][25] = 61;
		ActionTable_S[124][32] = 62;

		ActionTable_S[125][1] = 76;
		ActionTable_S[125][14] = 81;
		ActionTable_S[125][28] = 82;
		ActionTable_S[125][29] = 83;
		ActionTable_S[125][30] = 84;

		ActionTable_R[126][2] = 21;
		ActionTable_S[126][5] = 90;

		ActionTable_S[127][14] = 58;
		ActionTable_S[127][19] = 133;
		ActionTable_S[127][22] = 59;
		ActionTable_S[127][24] = 60;
		ActionTable_S[127][25] = 61;
		ActionTable_S[127][32] = 62;

		ActionTable_R[128][14] = 60;
		ActionTable_R[128][22] = 60;
		ActionTable_R[128][24] = 60;
		ActionTable_R[128][25] = 60;
		ActionTable_R[128][32] = 60;

		ActionTable_S[129][1] = 135;

		ActionTable_R[130][14] = 28;
		ActionTable_R[130][19] = 28;
		ActionTable_R[130][22] = 28;
		ActionTable_R[130][23] = 28;
		ActionTable_R[130][24] = 28;
		ActionTable_R[130][25] = 28;
		ActionTable_R[130][32] = 28;
		ActionTable_R[130][33] = 28;

		ActionTable_S[131][2] = 136;

		ActionTable_R[132][2] = 20;

		ActionTable_S[133][1] = 76;
		ActionTable_S[133][14] = 81;
		ActionTable_S[133][28] = 82;
		ActionTable_S[133][29] = 83;
		ActionTable_S[133][30] = 84;

		ActionTable_S[134][14] = 58;
		ActionTable_S[134][22] = 59;
		ActionTable_S[134][24] = 60;
		ActionTable_S[134][25] = 61;
		ActionTable_S[134][32] = 62;

		ActionTable_S[135][1] = 76;
		ActionTable_R[135][2] = 50;
		ActionTable_S[135][14] = 81;
		ActionTable_S[135][28] = 82;
		ActionTable_S[135][29] = 83;
		ActionTable_S[135][30] = 84;

		ActionTable_S[136][8] = 141;

		ActionTable_S[137][8] = 142;

		ActionTable_R[138][14] = 27;
		ActionTable_R[138][19] = 27;
		ActionTable_R[138][22] = 27;
		ActionTable_R[138][23] = 27;
		ActionTable_R[138][24] = 27;
		ActionTable_R[138][25] = 27;
		ActionTable_R[138][32] = 27;
		ActionTable_R[138][33] = 27;

		ActionTable_R[139][2] = 64;
		ActionTable_R[139][5] = 64;

		ActionTable_S[140][2] = 144;

		ActionTable_R[141][14] = 29;
		ActionTable_R[141][19] = 29;
		ActionTable_R[141][22] = 29;
		ActionTable_R[141][23] = 29;
		ActionTable_R[141][24] = 29;
		ActionTable_R[141][25] = 29;
		ActionTable_R[141][32] = 29;
		ActionTable_R[141][33] = 29;

		ActionTable_S[142][33] = 145;

		ActionTable_R[143][2] = 52;
		ActionTable_S[143][5] = 146;

		ActionTable_R[144][0] = 41;
		ActionTable_R[144][2] = 41;
		ActionTable_R[144][3] = 41;
		ActionTable_R[144][4] = 41;
		ActionTable_R[144][5] = 41;
		ActionTable_R[144][6] = 41;
		ActionTable_R[144][8] = 41;
		ActionTable_R[144][9] = 41;
		ActionTable_R[144][31] = 41;

		ActionTable_R[145][12] = 17;
		ActionTable_R[145][33] = 17;

		ActionTable_S[146][1] = 76;
		ActionTable_S[146][14] = 81;
		ActionTable_S[146][28] = 82;
		ActionTable_S[146][29] = 83;
		ActionTable_S[146][30] = 84;

		ActionTable_R[147][2] = 49;

		ActionTable_R[148][2] = 64;
		ActionTable_R[148][5] = 64;

		ActionTable_R[149][2] = 52;
		ActionTable_S[149][5] = 146;

		ActionTable_R[150][2] = 51;

		// GOTO TABLE COMPLETE
		GotoTable[0][0] = 1;
		GotoTable[0][1] = 2;

		GotoTable[2][2] = 3;
		GotoTable[2][3] = 4;
		GotoTable[2][8] = 5;

		GotoTable[4][4] = 8;

		GotoTable[5][3] = 10;
		GotoTable[5][8] = 5;

		GotoTable[11][9] = 13;

		GotoTable[13][10] = 15;

		GotoTable[18][11] = 21;
		GotoTable[18][14] = 22;

		GotoTable[21][12] = 25;
		GotoTable[21][18] = 26;

		GotoTable[22][11] = 28;
		GotoTable[22][14] = 22;

		GotoTable[23][15] = 29;

		GotoTable[26][12] = 34;
		GotoTable[26][18] = 26;

		GotoTable[35][15] = 38;

		GotoTable[38][9] = 41;

		GotoTable[43][9] = 45;

		GotoTable[44][5] = 46;
		GotoTable[44][15] = 47;
		GotoTable[44][17] = 48;

		GotoTable[46][6] = 50;

		GotoTable[48][5] = 52;
		GotoTable[48][15] = 47;
		GotoTable[48][17] = 48;

		GotoTable[49][19] = 53;

		GotoTable[50][7] = 54;

		GotoTable[53][15] = 56;
		GotoTable[53][20] = 57;

		GotoTable[54][27] = 64;

		GotoTable[58][33] = 67;

		GotoTable[60][30] = 69;

		GotoTable[62][7] = 71;

		GotoTable[65][26] = 73;

		GotoTable[66][21] = 74;

		GotoTable[68][13] = 77;
		GotoTable[68][16] = 78;
		GotoTable[68][23] = 79;
		GotoTable[68][24] = 80;
		GotoTable[68][29] = 85;
		GotoTable[68][34] = 86;

		GotoTable[71][27] = 64;

		GotoTable[73][25] = 91;

		GotoTable[75][13] = 77;
		GotoTable[75][16] = 78;
		GotoTable[75][23] = 93;
		GotoTable[75][24] = 80;
		GotoTable[75][29] = 85;
		GotoTable[75][34] = 86;

		GotoTable[76][13] = 94;
		GotoTable[76][24] = 80;
		GotoTable[76][29] = 85;

		GotoTable[81][9] = 102;

		GotoTable[87][13] = 77;
		GotoTable[87][16] = 78;
		GotoTable[87][23] = 103;
		GotoTable[87][24] = 80;
		GotoTable[87][29] = 85;
		GotoTable[87][34] = 86;

		GotoTable[90][15] = 105;

		GotoTable[92][5] = 106;
		GotoTable[92][15] = 47;
		GotoTable[92][17] = 48;

		GotoTable[95][24] = 109;
		GotoTable[95][29] = 85;

		GotoTable[96][24] = 110;
		GotoTable[96][29] = 85;

		GotoTable[97][13] = 111;
		GotoTable[97][24] = 80;
		GotoTable[97][29] = 85;

		GotoTable[98][13] = 112;
		GotoTable[98][24] = 80;
		GotoTable[98][29] = 85;

		GotoTable[99][13] = 113;
		GotoTable[99][24] = 80;
		GotoTable[99][29] = 85;
		GotoTable[99][34] = 114;

		GotoTable[100][1] = 115;

		GotoTable[101][29] = 116;

		GotoTable[106][22] = 121;

		GotoTable[115][27] = 122;

		GotoTable[118][1] = 124;

		GotoTable[120][26] = 126;

		GotoTable[121][7] = 127;

		GotoTable[123][31] = 129;

		GotoTable[124][27] = 130;

		GotoTable[125][13] = 77;
		GotoTable[125][16] = 78;
		GotoTable[125][23] = 131;
		GotoTable[125][24] = 80;
		GotoTable[125][29] = 85;
		GotoTable[125][34] = 86;

		GotoTable[126][25] = 132;

		GotoTable[127][27] = 64;

		GotoTable[128][28] = 134;

		GotoTable[133][13] = 77;
		GotoTable[133][16] = 78;
		GotoTable[133][23] = 137;
		GotoTable[133][24] = 80;
		GotoTable[133][29] = 85;
		GotoTable[133][34] = 86;

		GotoTable[134][27] = 138;

		GotoTable[135][13] = 77;
		GotoTable[135][16] = 78;
		GotoTable[135][23] = 139;
		GotoTable[135][24] = 80;
		GotoTable[135][29] = 85;
		GotoTable[135][32] = 140;
		GotoTable[135][34] = 86;

		GotoTable[139][36] = 143;

		GotoTable[143][35] = 147;

		GotoTable[146][13] = 77;
		GotoTable[146][16] = 78;
		GotoTable[146][23] = 148;
		GotoTable[146][24] = 80;
		GotoTable[146][29] = 85;
		GotoTable[146][34] = 86;

		GotoTable[148][36] = 149;

		GotoTable[149][35] = 150;

		RHS[0] = 1;
		RHS[1] = 3;
		RHS[2] = 2;
		RHS[3] = 16;
		RHS[4] = 2;
		RHS[5] = 0;
		RHS[6] = 8;
		RHS[7] = 2;
		RHS[8] = 0;
		RHS[9] = 2;
		RHS[10] = 0;
		RHS[11] = 4;
		RHS[12] = 2;
		RHS[13] = 0;
		RHS[14] = 3;
		RHS[15] = 2;
		RHS[16] = 0;
		RHS[17] = 19;
		RHS[18] = 4;
		RHS[19] = 0;
		RHS[20] = 5;
		RHS[21] = 0;
		RHS[22] = 1;
		RHS[23] = 1;
		RHS[24] = 2;
		RHS[25] = 0;
		RHS[26] = 3;
		RHS[27] = 9;
		RHS[28] = 7;
		RHS[29] = 9;
		RHS[30] = 5;
		RHS[31] = 1;
		RHS[32] = 1;
		RHS[33] = 3;
		RHS[34] = 3;
		RHS[35] = 1;
		RHS[36] = 3;
		RHS[37] = 1;
		RHS[38] = 3;
		RHS[39] = 1;
		RHS[40] = 4;
		RHS[41] = 8;
		RHS[42] = 1;
		RHS[43] = 1;
		RHS[44] = 1;
		RHS[45] = 3;
		RHS[46] = 1;
		RHS[47] = 3;
		RHS[48] = 3;
		RHS[49] = 3;
		RHS[50] = 0;
		RHS[51] = 4;
		RHS[52] = 0;
		RHS[53] = 0;
		RHS[54] = 0;
		RHS[55] = 0;
		RHS[56] = 0;
		RHS[57] = 0;
		RHS[58] = 0;
		RHS[59] = 0;
		RHS[60] = 0;
		RHS[61] = 0;
		RHS[62] = 0;
		RHS[63] = 0;
		RHS[64] = 0;

		LHS[0] = -1;
		LHS[1] = 0;
		LHS[2] = 2;
		LHS[3] = 4;
		LHS[4] = 3;
		LHS[5] = 3;
		LHS[6] = 8;
		LHS[7] = 10;
		LHS[8] = 10;
		LHS[9] = 11;
		LHS[10] = 11;
		LHS[11] = 14;
		LHS[12] = 5;
		LHS[13] = 5;
		LHS[14] = 17;
		LHS[15] = 12;
		LHS[16] = 12;
		LHS[17] = 18;
		LHS[18] = 20;
		LHS[19] = 20;
		LHS[20] = 25;
		LHS[21] = 25;
		LHS[22] = 15;
		LHS[23] = 15;
		LHS[24] = 7;
		LHS[25] = 7;
		LHS[26] = 27;
		LHS[27] = 27;
		LHS[28] = 27;
		LHS[29] = 27;
		LHS[30] = 27;
		LHS[31] = 23;
		LHS[32] = 23;
		LHS[33] = 13;
		LHS[34] = 13;
		LHS[35] = 13;
		LHS[36] = 24;
		LHS[37] = 24;
		LHS[38] = 29;
		LHS[39] = 29;
		LHS[40] = 29;
		LHS[41] = 29;
		LHS[42] = 29;
		LHS[43] = 29;
		LHS[44] = 29;
		LHS[45] = 16;
		LHS[46] = 16;
		LHS[47] = 34;
		LHS[48] = 34;
		LHS[49] = 32;
		LHS[50] = 32;
		LHS[51] = 35;
		LHS[52] = 35;
		LHS[53] = 1;
		LHS[54] = 6;
		LHS[55] = 9;
		LHS[56] = 19;
		LHS[57] = 21;
		LHS[58] = 22;
		LHS[59] = 26;
		LHS[60] = 28;
		LHS[61] = 30;
		LHS[62] = 33;
		LHS[63] = 31;
		LHS[64] = 36;

		scanner = new LexicalAnalyzer(filename);

	}

	public void printStack() {
		System.out.println(Arrays.toString(parseStack.toArray()));
	}

	public int topStack() {
		return (Integer) (parseStack.peek());
	}

	public void parse() throws Exception {
		int token;
		parseStack.push(new Integer(0));

		token = scanner.NextToken();

		while (true) {
			if (ActionTable_S[topStack()][token] != -1) {
				parseStack.push(ActionTable_S[topStack()][token]);
				LexicalAnalyzer.old_lexeme = LexicalAnalyzer.lexeme;
				token = scanner.NextToken();
			} else if (ActionTable_R[topStack()][token] != -1) {
				int reduce_by = ActionTable_R[topStack()][token];
				if (ActionTable_R[topStack()][token] == 0) {
					return;
				}

				int oldTopStack = topStack();
				for (int i = 0; i < RHS[ActionTable_R[oldTopStack][token]]; i++) {
					parseStack.pop();
				}
				parseStack
						.push(GotoTable[topStack()][LHS[ActionTable_R[oldTopStack][token]]]);
				codeGen(reduce_by);
			} else {
				throw new CompilerException("Error " + "(" + errorMessage
						+ ") " + "[" + LexicalAnalyzer.lineNumber + "].");
			}
		}

	}

	private void codeGen(int reduce_by) throws CompilerException {
		switch (reduce_by) {
		case 7:
			CodeGenerator.EXTENSION();
			break;
		case 8:
			CodeGenerator.POP();
			break;
		case 11:
			CodeGenerator.SET_STATIC();
			break;
		case 17:
			CodeGenerator.CHECK_RETURN_TYPE();
			break;
		case 27:
			CodeGenerator.JP();
			break;
		case 28:
			CodeGenerator.WHILE();
			break;
		case 29:
			CodeGenerator.PRINT();
			break;
		case 30:
			CodeGenerator.ASSIGN();
			break;
		case 33:
			CodeGenerator.ADD();
			break;
		case 34:
			CodeGenerator.SUB();
			break;
		case 36:
			CodeGenerator.MULT();
			break;
		case 39:
			CodeGenerator.PID();
			break;
		case 40:
			CodeGenerator.FACTOR_ADRS();
			break;
		case 41:
			CodeGenerator.CALL();
			break;
		case 42:
			CodeGenerator.PUSH_BOOLEAN();
			break;
		case 43:
			CodeGenerator.PUSH_BOOLEAN();
			break;
		case 44:
			CodeGenerator.PUSH_INT();
			break;
		case 45:
			CodeGenerator.AND();
			break;
		case 47:
			CodeGenerator.EQ();
			break;
		case 48:
			CodeGenerator.LT();
			break;
		case 53:
			CodeGenerator.SAVE();
			break;
		case 54:
			CodeGenerator.START_OF_MAIN();
			break;
		case 55:
			CodeGenerator.PUSH();
			break;
		case 56:
			CodeGenerator.INC_SCOPE();
			break;
		case 57:
			CodeGenerator.SET_DECLERATION_TO_FALSE();
			break;
		case 58:
			CodeGenerator.SET_METHOD_ADRS();
			break;
		case 59:
			CodeGenerator.SET_ARGUMENT();
			break;
		case 60:
			CodeGenerator.JPF_SAVE();
			break;
		case 61:
			CodeGenerator.LABEL();
			break;
		case 62:
			CodeGenerator.PID();
			break;
		case 63:
			CodeGenerator.FACTOR_INDEX();
			break;
		case 64:
			CodeGenerator.CHECK_ARG();
			break;
		default:
			break;
		}

	}

}
