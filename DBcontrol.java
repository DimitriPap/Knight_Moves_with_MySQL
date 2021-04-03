import java.sql.*;

public class DBcontrol implements control
{

    private String user;
    private int Source = 0;
    private int Destination;
    private int steps;


    public DBcontrol(String user)
    {
        this.user = user;
    }


    public DBcontrol(int Source, int Destination, int steps, String user)
    {
        this.Source = Source;
        this.Destination = Destination;
        this.user = user;
        this.steps = steps;
        pushedinDB(Source,Destination, steps, user);
    }


    //It searches User's history in database.
    @Override
    public void DatabaseSearch()
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/Chess_Knight_Moves?autoReconnect=true&useSSL=false" ,"root","root");
            Statement stmt = conn.createStatement();
            boolean entered = false;

            String sql= "SELECT * FROM " + user;
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println();


            while(rs.next())
            {
                int source = rs.getInt("source1");
                int dest = rs.getInt("dest");
                int steps = rs.getInt("steps");

                System.out.println("Starting point: " + source  + "\nDestination: " + dest + "\nSteps " + steps);
                System.out.println("-------------------");
                entered = true;
            }
            rs.close();

            if(!entered)
            {
                System.out.println("No previous moves have been detected!");
                System.out.println();
            }

        } catch (SQLException ex)
        {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //It saves new data into the database
    public void pushedinDB(int start1, int dest, int steps, String name)
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/Chess_Knight_Moves?autoReconnect=true&useSSL=false", "root", "root");
            Statement stmt = conn.createStatement();
            boolean entered = false;

            String sql = "INSERT INTO " + name + " values (" +start1+", "+ dest+ ", "+ steps +")";
            stmt.executeUpdate(sql);
        }catch (SQLException ex)
        {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    //Creates a new User table into the database
    public static void new_user(String name)
    {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/Chess_Knight_Moves?autoReconnect=true&useSSL=false" ,"root","root");
            Statement stmt = conn.createStatement();

            String sql= "CREATE TABLE " + name + " (source1 INTEGER, dest INTEGER, steps INTEGER)";
            stmt.executeUpdate(sql);


        } catch (SQLException ex)
        {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    @Override
    public void deleteAll()
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/Chess_Knight_Moves?autoReconnect=true&useSSL=false", "root", "root");
            Statement stmt = conn.createStatement();
            String sql= "truncate " + user ;
            stmt.executeUpdate(sql);

            System.out.println("All moves deleted successfully.");
            System.out.println();
        }catch (SQLException ex)
        {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

}
