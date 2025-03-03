package vehicle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.sql.*;
import database.DatabaseConnection;

public class Vehicle_Main{
    static Scanner sc=new Scanner(System.in);
    static Connection con;
    public Vehicle_Main(){
        try{
            con=DatabaseConnection.getDatabaseConnection();
        }
        catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, String> map = new HashMap<Integer, String>();

    //Hash Map
    public static void UserData() throws SQLException,ClassNotFoundException{
        ResultSet resultset=con.createStatement().executeQuery("select user_id,name from Customers");
        while(resultset.next()){
            int id=resultset.getInt(1);
            String name=resultset.getString(2);
            map.put(id,name);
        }
    }
    
    public static void Register() throws SQLException,ClassNotFoundException{
        String available="yes";
        PreparedStatement ps=con.prepareStatement("insert into vehicles values(?,?,?,?)");
        System.out.println("Enter ID");
            int id=sc.nextInt();
        System.out.println("Enter Name");
            sc.nextLine();
            String name=sc.nextLine();
        System.out.println("Enter Rate Per Day");
            int price=sc.nextInt();
        ps.setInt(1,id);
        ps.setString(2,name);
        ps.setInt(3,price);
        ps.setString(4,available);
            int rowAffected=ps.executeUpdate();
        if(rowAffected>0){
            System.out.println("Successful");
        }
        else{
            System.out.println("Failed To Add Vehicle");
        }
    }

        //stack
        class stack{
            int top=-1;
            int s[]=new int[100];
            void push(int x){
                top++;
                s[top]=x;
            }
            int pop(){
                int x=s[top];
                top--;
                return x;
            }
            boolean isEmpty(){
                if(top==-1){
                    return true;
                }
                return false;
            }
        }

    public static void Show_Rented_Vehicle_Sorted() throws SQLException,ClassNotFoundException{
        Statement st=con.createStatement();
        ResultSet resultset=st.executeQuery("select vehicle_id,end_date from Rental_History where status='on rent' order by end_date desc");
        stack s=new Vehicle_Main().new stack();
        while(resultset.next()){
            s.push(resultset.getInt(1));
        }
        while (!s.isEmpty()) {
            ResultSet resultset1=st.executeQuery("select vehicle_id,end_date from Rental_History where vehicle_id="+s.pop());
            resultset1.next();
            System.out.println();
            System.out.println("ID: "+resultset1.getInt(1));
            System.out.println("Return Date: "+resultset1.getString(2));
            System.out.println();
        }
    }   

    public static void Remove() throws SQLException,ClassNotFoundException{
        PreparedStatement prepareds=con.prepareStatement("delete from vehicles where vehicle_id=?");
        System.out.println("Enter ID of Vehicle to Remove");
            int id=sc.nextInt();
            Statement st=con.createStatement();
            st.executeUpdate("delete from Rental_History where vehicle_id="+id+" and status='on rent'");
            prepareds.setInt(1,id);
            int rowAffected=prepareds.executeUpdate();
        if(rowAffected>0){
            System.out.println("Successful");
        }
        else{
            System.out.println("Failed");
        }
    }

    public static void Show_All_Vehicle() throws SQLException,ClassNotFoundException{
        Statement st=con.createStatement();
        ResultSet resultset=st.executeQuery("select * from vehicles");
        while(resultset.next()){
            System.out.println();
            System.out.println("Name: "+resultset.getString(2));
            System.out.println("ID: "+resultset.getInt(1));
            System.out.println("Rate Per Day: "+resultset.getInt(3));
            System.out.println("Available: "+resultset.getString(4));
            System.out.println();
        }
    }

    public static void Show_Rented_Vehicle() throws SQLException,ClassNotFoundException{
        Statement st=con.createStatement();
        Statement stt=con.createStatement();
        ResultSet rs=st.executeQuery("select name,vehicle_id from vehicles where available='no'");
        while(rs.next()){
            System.out.println();
            System.out.println("ID: "+rs.getInt(2));
            System.out.println("Name: "+rs.getString(1));
            ResultSet rsTemp=stt.executeQuery("select end_date from Rental_History where vehicle_id="+rs.getInt(2)+" and status='on rent'");
            while(rsTemp.next()){
                System.out.println("We Will Get Our Vehicle Back on: "+rsTemp.getString(1));
            }
            rsTemp.close();
            System.out.println();
        }
    }

    public static void Show_Available_Vehicle() throws SQLException,ClassNotFoundException{
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("select * from vehicles where available='yes'");
        while(rs.next()){
            System.out.println();
            System.out.println("Name: "+rs.getString(2));
            System.out.println("ID: "+rs.getInt(1));
            System.out.println("Rate Per Day: "+rs.getInt(3));
            System.out.println();
        }
    }

    public static void Rent(int id) throws SQLException,ClassNotFoundException{
        System.out.println("List of Available Vehicles");
        Show_Available_Vehicle();

        System.out.println("Enter ID to Rent");
        int vehicle_id=sc.nextInt();
        System.out.println("Enter Start Date (YYYY-MM-DD)");
        String start_date=sc.next();
        System.out.println("Enter End Date (YYYY-MM-DD)");
        String end_date=sc.next();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(start_date, formatter);
        LocalDate endDate = LocalDate.parse(end_date, formatter);
        long divas = ChronoUnit.DAYS.between(startDate, endDate);
        int days = (int) divas;

        Statement st=con.createStatement();
        ResultSet resultset=st.executeQuery("select rate from vehicles where vehicle_id="+vehicle_id);
        resultset.next();
        int rate=resultset.getInt(1);
        int total_price=rate*days;
        System.out.println("Total Price: "+total_price);
        System.out.println("Place Order ?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int choice=sc.nextInt();
        if(choice==2){
            return;
        }
        st.executeUpdate("update vehicles set available='no' where vehicle_id="+vehicle_id);
        PreparedStatement prepareds=con.prepareStatement("insert into Rental_History(user_id,vehicle_id,start_date,end_date,total_cost,status) values(?,?,?,?,?,?)");
        prepareds.setInt(1,id);
        prepareds.setInt(2,vehicle_id);
        prepareds.setString(3,start_date);
        prepareds.setString(4,end_date);
        prepareds.setInt(5,total_price);
        prepareds.setString(6,"on rent");
        int rowAffected=prepareds.executeUpdate();
        if(rowAffected>0){
            System.out.println("Successful");
            System.out.println();
            System.out.println("We Hope You Enjoy Your Ride");
            System.out.println();
        }
        else{
            System.out.println("Failed");
        }
    }

    public static void Return(int id) throws SQLException,ClassNotFoundException{
        Statement st=con.createStatement();
        ResultSet resultset=st.executeQuery("select vehicle_id from Rental_History where user_id="+id);
        System.out.println("Enter ID to Return");
        int id_temp=sc.nextInt();
        int flag=0;
        while(resultset.next()){
            if(resultset.getInt(1)==id_temp){
                flag=1;
                System.out.println("Vehicle Found");
            }
            else{
                System.out.println("Checking...");
            }
        }
        if(flag==0){
            System.out.println("No Rental Found");
            return;
        }
        st.executeUpdate("update vehicles set available='yes' where vehicle_id="+id_temp);
        st.executeUpdate("update Rental_History set status='completed' where vehicle_id="+id_temp);
        System.out.println();
        System.out.println("Thank You For Using Our Service || See You Soon");
        System.out.println();
    }
}