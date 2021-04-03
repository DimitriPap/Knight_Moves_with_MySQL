import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
/*
     Chess board
| 0| 1| 2| 3| 4| 5| 6| 7|
-------------------------
| 8| 9|10|11|12|13|14|15|
-------------------------
|16|17|18|19|20|21|22|23|
-------------------------
|24|25|26|27|28|29|30|31|
-------------------------
|32|33|34|35|36|37|38|39|
-------------------------
|40|41|42|43|44|45|46|47|
-------------------------
|48|49|50|51|52|53|54|55|
-------------------------
|56|57|58|59|60|61|62|63|
 */
public class Knight_moves {
    public static void main(String[] args)
    {
        String name;
        int start, dest;
        Scanner kbd = new Scanner(System.in);

        //register - log in menu.
        System.out.println();
        System.out.println("Type 1 for an existing user\nType 2 for a new user");
        int choice1 = kbd.nextInt();

        if(choice1 == 2)
        {
            String new_U;
            System.out.print("Create new username: ");
            new_U = kbd.next();
            DBcontrol.new_user(new_U);
            System.out.println("Username " + new_U + " created");
        }
        System.out.println();
        System.out.print("Login with username: ");
        name = kbd.next();
        int choice = 0;
        boolean exit = false;
        System.out.println();
        System.out.println("Welcome " + name + "!");

        //Main menu for the user
        while(!exit)
        {
            System.out.println("Pick one of the following choices");
            System.out.print("1. Look at previous moves\n2. New move\n3. Delete all previous moves\n4. Exit\n");
            choice = kbd.nextInt();

            //Look previous moves
            if(choice == 1)
            {
                DBcontrol currentData = new DBcontrol(name);
                currentData.DatabaseSearch();
            }

            //New move
            else if(choice == 2)
            {
                //Prints a table from 1 to 63
                System.out.println();
                int nums = 0;
                for(int i=0; i<=7; i++)
                {
                    for(int j=0; j<=7; j++)
                    {
                        if(nums < 10)
                        {
                            System.out.print("| " + nums++ + "|");
                        }
                        else
                        {
                            System.out.print("|" + nums++ + "|");
                        }
                        if(j==7)
                        {
                            System.out.println();
                        }

                    }
                    System.out.println("-------------------------");
                }

                //Asking for user input (start - destination) point
                System.out.println();
                System.out.print("Enter Starting point: ");
                start = kbd.nextInt();

                System.out.print("Enter destination: ");
                dest = kbd.nextInt();

                //Calculating the steps in "finalized" method using breadth first search algorithm
                int st = finalized( start,dest);
                System.out.println("\nThe required steps are: " + st);

                //Create an object and pushing the data into MySQL database
                DBcontrol n = new DBcontrol(start, dest, st, name);
            }

            //Delete all previous moves from the Database
            else if(choice == 3)
            {
                DBcontrol n = new DBcontrol(name);
                n.deleteAll();
            }

            //exit
            else if(choice == 4)
            {
                exit = true;
            }
        }
    }


    public static int finalized(int src, int dest)
    {
        //Calculating the required steps from source to destination

        Queue<Integer> q = new LinkedList<>();
        int row = 0, col =0;
        int destination_row =0, destination_col =0;

        int[][] table = new int[8][8];
        int SrcLocator = 0;

        //Look numeric values for source and destination
        for(int i=0; i<table.length; i++)
        {
            for(int j=0; j<table[i].length; j++)
            {
                if(SrcLocator == src)
                {
                    row = i;
                    col = j;
                }
                if(SrcLocator == dest)
                {
                    destination_row = i;
                    destination_col = j;
                }
                SrcLocator++;
            }
        }

        int originalRow = row, originalCol = col;

        //If users passes as input the same digits, then I return 0
        if(src == dest)
        {
            return 0;
        }

        //If users provides not valid cells, then I return -1
        if(src < 0 || src > 63 || dest < 1 || dest > 63)
        {
            return -1;
        }

        //Knight moves on the board. vertical or horizontal,  1 then 2, or 2 then 1
        int[] doIni = {-2,-1,1,2,2,1,-1,-2};
        int[] doInj = {1,2,2,1,-1,-2,-2,-1};


        //We start with everything zero, which means everything is unvisited.
        while(isZero(table))
        {
            // Zero means NOT visited
            for (int i = 0; i < doIni.length; i++)
            {
                try{
                    if (table[row + doIni[i]][col + doInj[i]] == 0)
                    {
                        q.add(col + doInj[i]);
                        q.add(row + doIni[i]);

                        //Add to the existing value +1 (to add the previous steps too)
                        table[row + doIni[i]][col + doInj[i]] = table[row][col]+1;
                    }
                }catch(ArrayIndexOutOfBoundsException e){};
            }


            if(row == originalRow && col == originalCol)
            {
                table[originalRow][originalCol] = 99;
            }

            //dequeue elements
            col= q.remove();
            row = q.remove();
        }

        //Print finalized board
        System.out.println();
        for (int i = 0; i < table.length; i++)
        {
            for (int j = 0; j < table[i].length; j++)
            {
                System.out.print(table[i][j]+ " ");
            }
            System.out.println();
        }

        //Return number of steps for desired cell
        return table[destination_row][destination_col];
    }


    public static boolean isZero(int[][] table)
    {
        for (int i = 0; i < table.length; i++)
        {
            for (int j = 0; j < table[i].length; j++)
            {
                if (table[i][j] == 0)
                {
                    return true;
                }
            }
        }

        return false;
    }
}