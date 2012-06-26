class Quine
{
    //
    // Data declaration
    int truthTable[];
    int binary[], bin_limit;
    int group_2[][], group_2xlim;
    int group_4[][], group_4xlim;
    int group_8[][], group_8xlim;
    int final_group_4[][], fingr_lim_4, flags[];
    int select_rows_4[][], sel_rows_4_lim;
    int final_group_8[][], fingr_lim_8;
    int select_rows_8[][], sel_rows_8_lim;
    int select_rows_2[][], sel_rows_2_lim;
    int n, m, l, expr_lim;
    // char expression[];
    int not_consider[][], not_cons_lim;
    int consider_8[][];
    int select_valid_4[][], sel_val_lim;
    int select_valid_8[][], sel_val_lim_8;

    Quine()
    {
        truthTable = new int[16];
        binary = new int[16];
        group_2 = new int[100][2];
        group_4 = new int[100][4];
        group_8 = new int[100][8];
        final_group_4 = new int[100][4];
        flags = new int[16];
        select_rows_4 = new int[100][4];
        final_group_8 = new int[100][8];
        select_rows_8 = new int[100][8];
        select_rows_2 = new int[100][2];
        // expression=new char [200];
        not_consider = new int[100][4];
        consider_8 = new int[100][8];
        select_valid_4 = new int[100][4];
        select_valid_8 = new int[100][8];


        not_cons_lim = 0;
        sel_val_lim = 0;
        sel_val_lim_8 = 0;

        for (n = 0; n < 16; n++)
        {
            truthTable[n] = 0;
            binary[n] = 0;
            flags[n] = 0;
        }
        group_2xlim = 0;
        group_4xlim = 0;
        group_8xlim = 0;
        bin_limit = 0;
        fingr_lim_4 = 0;
        sel_rows_2_lim = 0;
        sel_rows_4_lim = 0;
        sel_rows_8_lim = 0;
        expr_lim = 0;

        not_consider[0][0] = 1;
        not_consider[0][1] = 3;
        not_consider[0][2] = 12;
        not_consider[0][3] = 14;
        not_consider[1][0] = 1;
        not_consider[1][1] = 6;
        not_consider[1][2] = 9;
        not_consider[1][3] = 14;
        not_consider[2][0] = 2;
        not_consider[2][1] = 3;
        not_consider[2][2] = 12;
        not_consider[2][3] = 13;
        not_consider[3][0] = 2;
        not_consider[3][1] = 6;
        not_consider[3][2] = 9;
        not_consider[3][3] = 13;
        not_consider[4][0] = 2;
        not_consider[4][1] = 5;
        not_consider[4][2] = 10;
        not_consider[4][3] = 13;
        not_consider[5][0] = 4;
        not_consider[5][1] = 5;
        not_consider[5][2] = 10;
        not_consider[5][3] = 11;
        not_consider[6][0] = 4;
        not_consider[6][1] = 6;
        not_consider[6][2] = 9;
        not_consider[0][3] = 11;

        consider_8[0][0] = 0;
        consider_8[0][1] = 1;
        consider_8[0][2] = 4;
        consider_8[0][3] = 5;
        consider_8[0][4] = 8;
        consider_8[0][5] = 9;
        consider_8[0][6] = 12;
        consider_8[0][7] = 13;
        consider_8[1][0] = 0;
        consider_8[1][1] = 2;
        consider_8[1][2] = 4;
        consider_8[1][3] = 6;
        consider_8[1][4] = 8;
        consider_8[1][5] = 10;
        consider_8[1][6] = 12;
        consider_8[1][7] = 14;
        consider_8[2][0] = 0;
        consider_8[2][1] = 1;
        consider_8[2][2] = 2;
        consider_8[2][3] = 3;
        consider_8[2][4] = 4;
        consider_8[2][5] = 5;
        consider_8[2][6] = 6;
        consider_8[2][7] = 7;
        consider_8[3][0] = 0;
        consider_8[3][1] = 1;
        consider_8[3][2] = 2;
        consider_8[3][3] = 3;
        consider_8[3][4] = 8;
        consider_8[3][5] = 9;
        consider_8[3][6] = 10;
        consider_8[3][7] = 11;
        consider_8[4][0] = 1;
        consider_8[4][1] = 3;
        consider_8[4][2] = 5;
        consider_8[4][3] = 7;
        consider_8[4][4] = 9;
        consider_8[4][5] = 11;
        consider_8[4][6] = 13;
        consider_8[4][7] = 15;
        consider_8[5][0] = 4;
        consider_8[5][1] = 5;
        consider_8[5][2] = 6;
        consider_8[5][3] = 7;
        consider_8[5][4] = 12;
        consider_8[5][5] = 13;
        consider_8[5][6] = 14;
        consider_8[5][7] = 15;
        consider_8[6][0] = 8;
        consider_8[6][1] = 9;
        consider_8[6][2] = 10;
        consider_8[6][3] = 11;
        consider_8[6][4] = 12;
        consider_8[6][5] = 13;
        consider_8[6][6] = 14;
        consider_8[6][7] = 15;
        consider_8[7][0] = 2;
        consider_8[7][1] = 3;
        consider_8[7][2] = 6;
        consider_8[7][3] = 7;
        consider_8[7][4] = 10;
        consider_8[7][5] = 11;
        consider_8[7][6] = 14;
        consider_8[7][7] = 15;

    }

    // get the truth values
    void gettruth(int truth[], boolean state, int varChoice)
    {
        int limit = 1;
        if (varChoice != 1)
        {
            for (int i = 0; i < varChoice; i++)
                limit = 2 * limit;
        }

        if (state == true)
        {
            for (int i = 0; i < 16; i++)
                truthTable[i] = truth[i];
        }
        else
        {
            for (int i = 0; i < limit; i++)
                truthTable[i] = truth[i] ^ 1;

            for (int i = limit; i < 16; i++)
                truthTable[i] = truth[i];

        }

        for (n = 0; n < 16; n++)
        {
            System.out.println(truthTable[n]);
            if (truthTable[n] == 1)
                binary[bin_limit++] = n;
            else
                flags[n] = 1;
        }
        // System.exit(0);
    }

    // calculate sum of number of ones
    int sumdig(int no)
    {
        int sum = 0;
        int quotient;
        while (no != 0)
        {
            int rem = no % 2;
            quotient = no / 2;
            no = quotient;
            sum = sum + rem;
        }
        return sum;
    }

    // making the groups of numbers such that differance between two numbers should be in powers of 2
    void make_groups2()
    {
        for (n = 0; n < bin_limit; n++)
        {
            for (m = 0; m < bin_limit; m++)
                if (((binary[m] - binary[n] == 1) || (binary[m] - binary[n] == 2) || (binary[m] - binary[n] == 4) || (binary[m] - binary[n] == 8))
                        && ((sumdig(binary[m])) - (sumdig(binary[n])) == 1))
                {
                    group_2[group_2xlim][0] = binary[n];
                    group_2[group_2xlim++][1] = binary[m];
                }
        }
    }


    // Now making the groups of the numbers having similar defferences
    void make_groups4()
    {
        for (n = 0; n < group_2xlim; n++)
        {
            for (m = n + 1; m < group_2xlim; m++)
                if ((group_2[n][0] - group_2[n][1] == group_2[m][0] - group_2[m][1]) && ((sumdig(group_2[m][0]) - sumdig(group_2[n][0])) == 1))
                {
                    group_4[group_4xlim][0] = group_2[n][0];
                    group_4[group_4xlim][1] = group_2[n][1];
                    group_4[group_4xlim][2] = group_2[m][0];
                    group_4[group_4xlim++][3] = group_2[m][1];
                }
        }
    }

    // sort each group
    void sort_4()
    {
        for (n = 0; n < group_4xlim; n++)
        {
            for (m = 0; m < 4; m++)
            {
                for (l = 0; l < 4; l++)
                    if (group_4[n][m] <= group_4[n][l])
                    {
                        int temp = group_4[n][m];
                        group_4[n][m] = group_4[n][l];
                        group_4[n][l] = temp;
                    }
            }
        }
    }


    // cancel the similar groups
    void cancel_sim_4()
    {
        int flag = 1;
        n = 0;

        for (l = n; l < group_4xlim; l++)
        {
            for (m = 0; m < n; m++)
            {
                if ((group_4[l][0] == group_4[m][0]) && (group_4[l][1] == group_4[m][1]) && (group_4[l][2] == group_4[m][2])
                        && (group_4[l][3] == group_4[m][3]))
                {
                    flag = 0;
                    break;
                } // if
            } // for

            if (flag != 0)
            {
                for (int p = 0; p < 4; p++)
                    final_group_4[fingr_lim_4][p] = group_4[l][p];
                fingr_lim_4++;
            }
            flag = 1;
            n++;
        }
    }



    void selects_valid_4()
    {
        int flag = 0;
        int count = 0;
        for (int i = 0; i < fingr_lim_4; i++)
        {
            for (int j = 0; j <= 6; j++)
            {
                count = 0;
                for (int k = 0; k < 4; k++)
                {
                    if (final_group_4[i][k] == not_consider[j][k])
                        count++;
                }
                if (count != 4)
                    flag = 0;
                else
                {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
            {
                select_valid_4[sel_val_lim][0] = final_group_4[i][0];
                select_valid_4[sel_val_lim][1] = final_group_4[i][1];
                select_valid_4[sel_val_lim][2] = final_group_4[i][2];
                select_valid_4[sel_val_lim++][3] = final_group_4[i][3];
            }
        }
    }


    // Now making the groups of 8 the numbers having similar defferences
    void make_groups8()
    {
        for (n = 0; n < sel_val_lim; n++)
        {
            for (m = n + 1; m < sel_val_lim; m++)
            {
                if ((sumdig(select_valid_4[m][0]) - sumdig(select_valid_4[n][0])) == 1)
                {
                    group_8[group_8xlim][0] = select_valid_4[n][0];
                    group_8[group_8xlim][1] = select_valid_4[n][1];
                    group_8[group_8xlim][2] = select_valid_4[n][2];
                    group_8[group_8xlim][3] = select_valid_4[n][3];
                    group_8[group_8xlim][4] = select_valid_4[m][0];
                    group_8[group_8xlim][5] = select_valid_4[m][1];
                    group_8[group_8xlim][6] = select_valid_4[m][2];
                    group_8[group_8xlim][7] = select_valid_4[m][3];
                }

                int match = 0;
                for (int i = 0; i < 8; i++)
                    for (int j = i + 1; j < 8; j++)
                        if (group_8[group_8xlim][i] == group_8[group_8xlim][j])
                            match++;
                if (match == 0)
                {
                    for (int k = 0; k < 8; k++)
                        flags[group_8[group_8xlim][k]] = 1;
                    group_8xlim++;
                }
            }
        }
    }


    // sort each group
    void sort_8()
    {
        for (n = 0; n < group_8xlim; n++)
        {
            for (m = 0; m < 8; m++)
            {
                for (l = 0; l < 8; l++)
                    if (group_8[n][m] <= group_8[n][l])
                    {
                        int temp = group_8[n][m];
                        group_8[n][m] = group_8[n][l];
                        group_8[n][l] = temp;
                    }
            }
        }
    }


    // Select the functions such that all minterms are covered
    void select_8_rows()
    {
        for (int i = 0; i < 16; i++)
        {
            flags[i] = 0;
            if (truthTable[i] == 0)
                flags[i] = 1;
        }


        for (int temp = 0; temp < 8; temp++)
            for (int pass = 0; pass < 8; pass++)
            {
                int count = 0;

                if (pass == temp)
                {
                    for (int i = 0; i < sel_val_lim_8; i++)
                    {
                        for (int j = 0; j < 8; j++)
                        {
                            if (flags[select_valid_8[i][j]] == 0)
                                count++;
                        }

                        if (count == 8 - temp)
                        {
                            for (int n = 0; n < 8; n++)
                            {
                                flags[select_valid_8[i][n]] = 1;
                                select_rows_8[sel_rows_8_lim][n] = select_valid_8[i][n];
                                count = 0;
                            }
                            sel_rows_8_lim++;
                        }
                        else
                            count = 0;
                    }
                }
            }
    }


    void selects_valid_8()
    {
        int flag = 0;
        int count = 0;
        for (int i = 0; i < group_8xlim; i++)
        {
            for (int j = 0; j <= 7; j++)
            {
                count = 0;
                for (int k = 0; k < 8; k++)
                {
                    if (group_8[i][k] == consider_8[j][k])
                        count++;
                }
                if (count == 8)
                {
                    flag = 0;
                    break;
                }
                else
                    flag = 1;
            }
            if (flag == 0)
            {
                select_valid_8[sel_val_lim_8][0] = group_8[i][0];
                select_valid_8[sel_val_lim_8][1] = group_8[i][1];
                select_valid_8[sel_val_lim_8][2] = group_8[i][2];
                select_valid_8[sel_val_lim_8][3] = group_8[i][3];
                select_valid_8[sel_val_lim_8][4] = group_8[i][4];
                select_valid_8[sel_val_lim_8][5] = group_8[i][5];
                select_valid_8[sel_val_lim_8][6] = group_8[i][6];
                select_valid_8[sel_val_lim_8++][7] = group_8[i][7];
            }
        }
    }


    void update_flags_sel_valid_8()
    {
        for (int i = 0; i < 16; i++)
        {
            flags[i] = 0;
            if (truthTable[i] == 0)
                flags[i] = 1;
        }

        for (int j = 0; j < sel_val_lim_8; j++)
            for (int k = 0; k < 8; k++)
                flags[select_valid_8[j][k]] = 1;
    }


    // Select the functions such that all minterms are covered
    void select_4_rows()
    {
        for (int temp = 0; temp < 4; temp++)
            for (int pass = 0; pass < 4; pass++)
            {
                int count = 0;

                if (pass == temp)
                {
                    for (int i = 0; i < sel_val_lim; i++)
                    {
                        for (int j = 0; j < 4; j++)
                        {
                            if (flags[select_valid_4[i][j]] == 0)
                                count++;
                        }

                        if (count == 4 - temp)
                        {
                            for (int n = 0; n < 4; n++)
                            {
                                flags[select_valid_4[i][n]] = 1;
                                select_rows_4[sel_rows_4_lim][n] = select_valid_4[i][n];
                                count = 0;
                            }
                            sel_rows_4_lim++;
                        }
                        else
                            count = 0;
                    }
                }
            }
    }

    // Select the functions such that all minterms are covered (2 groups)
    void select_2_rows()
    {
        for (int temp = 0; temp < 2; temp++)
            for (int pass = 0; pass < 2; pass++)
            {
                int count = 0;

                if (pass == temp)
                {
                    for (int i = 0; i < group_2xlim; i++)
                    {
                        for (int j = 0; j < 2; j++)
                        {
                            if (flags[group_2[i][j]] == 0)
                                count++;
                        }

                        if (count == 2 - temp)
                        {
                            for (int n = 0; n < 2; n++)
                            {
                                flags[group_2[i][n]] = 1;
                                select_rows_2[sel_rows_2_lim][n] = group_2[i][n];
                                count = 0;
                            }
                            sel_rows_2_lim++;
                        }
                        else
                            count = 0;
                    }
                }
            }
    }




    // find expression minterm
    String find_expression(char expression[], int varChoice)
    {
        expr_lim = 0;

        // expression[expr_lim++]='(';

        for (int i = 0; i < sel_rows_8_lim; i++)
        {
            int anding = select_rows_8[i][0] & select_rows_8[i][1] & select_rows_8[i][2] & select_rows_8[i][3] & select_rows_8[i][4]
                    & select_rows_8[i][5] & select_rows_8[i][6] & select_rows_8[i][7];
            int oring = select_rows_8[i][0] | select_rows_8[i][1] | select_rows_8[i][2] | select_rows_8[i][3] | select_rows_8[i][4]
                    | select_rows_8[i][5] | select_rows_8[i][6] | select_rows_8[i][7];
            int exor = anding ^ oring;

            int exor_array[] = new int[varChoice];

            for (int n = 0; n < varChoice; n++)
                exor_array[n] = 0;

            n = varChoice - 1;
            while (exor != 0)
            {
                int rem = exor % 2;
                int qtn = exor / 2;
                exor = qtn;
                exor_array[n] = rem;
                --n;
            }

            int no4_array[] = new int[varChoice];
            for (n = 0; n < varChoice; n++)
                no4_array[n] = 0;
            n = varChoice - 1;
            int abhya = select_rows_8[i][3];
            while (abhya != 0)
            {
                int rem = abhya % 2;
                int qtn = abhya / 2;
                abhya = qtn;
                no4_array[n] = rem;
                --n;
            }

            for (n = 0; n < varChoice; n++)
            {
                if (exor_array[n] == 0)
                {
                    if (n == 0)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'A';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'A';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 1)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'B';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'B';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 2)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'C';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'C';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 3)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'D';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'D';
                            // expression[expr_lim++]='&';
                        }
                    }
                }
            }
            // expr_lim;
            // expression[expr_lim++]=')';
            expression[expr_lim++] = '+';
            // expression[expr_lim++]='(';
        }


        for (int i = 0; i < sel_rows_4_lim; i++)
        {
            int anding = select_rows_4[i][0] & select_rows_4[i][1] & select_rows_4[i][2] & select_rows_4[i][3];
            int oring = select_rows_4[i][0] | select_rows_4[i][1] | select_rows_4[i][2] | select_rows_4[i][3];
            int exor = anding ^ oring;

            int exor_array[] = new int[varChoice];
            for (int n = 0; n < varChoice; n++)
                exor_array[n] = 0;

            n = varChoice - 1;
            while (exor != 0)
            {
                int rem = exor % 2;
                int qtn = exor / 2;
                exor = qtn;
                exor_array[n] = rem;
                --n;
            }

            int no4_array[] = new int[varChoice];
            for (n = 0; n < varChoice; n++)
                no4_array[n] = 0;
            n = varChoice - 1;
            int abhya = select_rows_4[i][3];
            while (abhya != 0)
            {
                int rem = abhya % 2;
                int qtn = abhya / 2;
                abhya = qtn;
                no4_array[n] = rem;
                --n;
            }

            for (n = 0; n < varChoice; n++)
            {
                if (exor_array[n] == 0)
                {
                    if (n == 0)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'A';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'A';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 1)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'B';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'B';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 2)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'C';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'C';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 3)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'D';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'D';
                            // expression[expr_lim++]='&';
                        }
                    }
                }
            }
            // expr_lim;
            // expression[expr_lim++]=')';
            expression[expr_lim++] = '+';
            // expression[expr_lim++]='(';
        }


        for (int i = 0; i < sel_rows_2_lim; i++)
        {
            int anding = select_rows_2[i][0] & select_rows_2[i][1];
            int oring = select_rows_2[i][0] | select_rows_2[i][1];
            int exor = anding ^ oring;

            int exor_array[] = new int[4];
            for (int n = 0; n < varChoice; n++)
                exor_array[n] = 0;

            n = varChoice - 1;
            while (exor != 0)
            {
                int rem = exor % 2;
                int qtn = exor / 2;
                exor = qtn;
                exor_array[n] = rem;
                --n;
            }

            int no4_array[] = new int[varChoice];
            for (n = 0; n < varChoice; n++)
                no4_array[n] = 0;
            n = varChoice - 1;
            int abhya = select_rows_2[i][1];
            while (abhya != 0)
            {
                int rem = abhya % 2;
                int qtn = abhya / 2;
                abhya = qtn;
                no4_array[n] = rem;
                --n;
            }

            for (n = 0; n < varChoice; n++)
            {
                if (exor_array[n] == 0)
                {
                    if (n == 0)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'A';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'A';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 1)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'B';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'B';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 2)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'C';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'C';
                            // expression[expr_lim++]='&';
                        }
                    }
                    if (n == 3)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'D';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'D';
                            // expression[expr_lim++]='&';
                        }
                    }
                }
            }
            // expr_lim;
            // expression[expr_lim++]=')';
            expression[expr_lim++] = '+';
            // expression[expr_lim++]='(';
        }


        for (int i = 0; i < 16; i++)
        {
            int no4_array[] = new int[varChoice];
            for (n = 0; n < varChoice; n++)
                no4_array[n] = 0;
            int num = i;
            if (flags[i] == 0)
            {
                n = varChoice - 1;
                while (num != 0)
                {
                    int rem = num % 2;
                    int qtn = num / 2;
                    num = qtn;
                    no4_array[n] = rem;
                    --n;
                }

                for (n = 0; n < varChoice; n++)
                {
                    if (n == 0)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'A';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'A';
                            // expression[expr_lim++]='&';
                        }
                    }

                    if (n == 1)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'B';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'B';
                            // expression[expr_lim++]='&';
                        }
                    }

                    if (n == 2)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'C';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'C';
                            // expression[expr_lim++]='&';
                        }
                    }

                    if (n == 3)
                    {
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'D';
                            // expression[expr_lim++]='&';
                        }
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = 'D';
                            // expression[expr_lim++]='&';
                        }
                    }


                }
                // expr_lim;
                // expression[expr_lim++]=')';
                expression[expr_lim++] = '+';
                // expression[expr_lim++]='(';
            }
        }


        String ret = new String(expression, 0, expr_lim);

        return ret;

    }



    // find expression maxterm
    String find_expression_maxterm(char expression[], int varChoice)
    {
        expr_lim = 0;

        expression[expr_lim++] = '(';

        // System.out.println(sel_rows_8_lim);
        // System.exit(0);

        for (int i = 0; i < sel_rows_8_lim; i++)
        {
            int anding = select_rows_8[i][0] & select_rows_8[i][1] & select_rows_8[i][2] & select_rows_8[i][3] & select_rows_8[i][4]
                    & select_rows_8[i][5] & select_rows_8[i][6] & select_rows_8[i][7];
            int oring = select_rows_8[i][0] | select_rows_8[i][1] | select_rows_8[i][2] | select_rows_8[i][3] | select_rows_8[i][4]
                    | select_rows_8[i][5] | select_rows_8[i][6] | select_rows_8[i][7];
            int exor = anding ^ oring;

            int exor_array[] = new int[varChoice];

            for (int n = 0; n < varChoice; n++)
                exor_array[n] = 0;

            n = varChoice - 1;
            while (exor != 0)
            {
                int rem = exor % 2;
                int qtn = exor / 2;
                exor = qtn;
                exor_array[n] = rem;
                --n;
            }

            int no4_array[] = new int[varChoice];
            for (n = 0; n < varChoice; n++)
                no4_array[n] = 0;
            n = varChoice - 1;
            int abhya = select_rows_8[i][3];
            while (abhya != 0)
            {
                int rem = abhya % 2;
                int qtn = abhya / 2;
                abhya = qtn;
                no4_array[n] = rem;
                --n;
            }

            for (n = 0; n < varChoice; n++)
            {
                if (exor_array[n] == 0)
                {
                    if (n == 0)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'A';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'A';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 1)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'B';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'B';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 2)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'C';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'C';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 3)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'D';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'D';
                            expression[expr_lim++] = '+';
                        }
                    }
                }
            }
            --expr_lim;
            expression[expr_lim++] = ')';
            expression[expr_lim++] = '.';
            expression[expr_lim++] = '(';
        }


        for (int i = 0; i < sel_rows_4_lim; i++)
        {
            int anding = select_rows_4[i][0] & select_rows_4[i][1] & select_rows_4[i][2] & select_rows_4[i][3];
            int oring = select_rows_4[i][0] | select_rows_4[i][1] | select_rows_4[i][2] | select_rows_4[i][3];
            int exor = anding ^ oring;

            int exor_array[] = new int[varChoice];
            for (int n = 0; n < varChoice; n++)
                exor_array[n] = 0;

            n = varChoice - 1;
            while (exor != 0)
            {
                int rem = exor % 2;
                int qtn = exor / 2;
                exor = qtn;
                exor_array[n] = rem;
                --n;
            }

            int no4_array[] = new int[varChoice];
            for (n = 0; n < varChoice; n++)
                no4_array[n] = 0;
            n = varChoice - 1;
            int abhya = select_rows_4[i][3];
            while (abhya != 0)
            {
                int rem = abhya % 2;
                int qtn = abhya / 2;
                abhya = qtn;
                no4_array[n] = rem;
                --n;
            }

            for (n = 0; n < varChoice; n++)
            {
                if (exor_array[n] == 0)
                {
                    if (n == 0)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'A';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'A';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 1)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'B';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'B';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 2)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'C';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'C';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 3)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'D';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'D';
                            expression[expr_lim++] = '+';
                        }
                    }
                }
            }
            --expr_lim;
            expression[expr_lim++] = ')';
            expression[expr_lim++] = '.';
            expression[expr_lim++] = '(';
        }


        for (int i = 0; i < sel_rows_2_lim; i++)
        {
            int anding = select_rows_2[i][0] & select_rows_2[i][1];
            int oring = select_rows_2[i][0] | select_rows_2[i][1];
            int exor = anding ^ oring;

            int exor_array[] = new int[varChoice];
            for (int n = 0; n < varChoice; n++)
                exor_array[n] = 0;

            n = varChoice - 1;
            while (exor != 0)
            {
                int rem = exor % 2;
                int qtn = exor / 2;
                exor = qtn;
                exor_array[n] = rem;
                --n;
            }

            int no4_array[] = new int[varChoice];
            for (n = 0; n < varChoice; n++)
                no4_array[n] = 0;
            n = varChoice - 1;
            int abhya = select_rows_2[i][1];
            while (abhya != 0)
            {
                int rem = abhya % 2;
                int qtn = abhya / 2;
                abhya = qtn;
                no4_array[n] = rem;
                --n;
            }

            for (n = 0; n < varChoice; n++)
            {
                if (exor_array[n] == 0)
                {
                    if (n == 0)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'A';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'A';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 1)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'B';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'B';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 2)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'C';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'C';
                            expression[expr_lim++] = '+';
                        }
                    }
                    if (n == 3)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'D';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'D';
                            expression[expr_lim++] = '+';
                        }
                    }
                }
            }
            --expr_lim;
            expression[expr_lim++] = ')';
            expression[expr_lim++] = '.';
            expression[expr_lim++] = '(';
        }


        for (int i = 0; i < 16; i++)
        {
            int no4_array[] = new int[varChoice];
            for (n = 0; n < varChoice; n++)
                no4_array[n] = 0;
            int num = i;
            if (flags[i] == 0)
            {
                n = varChoice - 1;
                while (num != 0)
                {
                    int rem = num % 2;
                    int qtn = num / 2;
                    num = qtn;
                    no4_array[n] = rem;
                    --n;
                }

                for (n = 0; n < varChoice; n++)
                {
                    if (n == 0)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'A';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'A';
                            expression[expr_lim++] = '+';
                        }
                    }

                    if (n == 1)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'B';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'B';
                            expression[expr_lim++] = '+';
                        }
                    }

                    if (n == 2)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'C';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'C';
                            expression[expr_lim++] = '+';
                        }
                    }

                    if (n == 3)
                    {
                        if (no4_array[n] == 1)
                        {
                            expression[expr_lim++] = '~';
                            expression[expr_lim++] = 'D';
                            expression[expr_lim++] = '+';
                        }
                        if (no4_array[n] == 0)
                        {
                            expression[expr_lim++] = 'D';
                            expression[expr_lim++] = '+';
                        }
                    }


                }
                --expr_lim;
                expression[expr_lim++] = ')';
                expression[expr_lim++] = '.';
                expression[expr_lim++] = '(';
            }
        }


        String ret = new String(expression, 0, expr_lim);

        return ret;

    }




}
