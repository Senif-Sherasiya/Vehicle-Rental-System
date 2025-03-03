package user;

import java.util.*;
import java.sql.*;
import vehicle.Vehicle_Main;
import database.DatabaseConnection;

public class User{
    static Scanner sc=new Scanner(System.in);
    static Connection con;
    public User(){
        try{
            con=DatabaseConnection.getDatabaseConnection();
        }
        catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static int StartUser() throws SQLException,ClassNotFoundException{
        
        System.out.println("1. User Login");
        System.out.println("2. Sign Up");
        
            int c=sc.nextInt();
        switch(c){
            case 1:
                return Login();
            case 2:
                return Add();
            default:
                System.out.println("Invalid Choice");
                return 0;
        }    
        
    }

    public static int Login() throws SQLException,ClassNotFoundException{
        System.out.println("Enter User ID");
            id=sc.nextInt();
            
        if(Vehicle_Main.map.containsKey(id)){
            System.out.println("User Login Successful");
            System.out.println();
            return id;
        }
        else{
            System.out.println("Invalid User ID");
            return 0;
        }
    }

    static int id;
    public static int Add() throws SQLException,ClassNotFoundException{
        PreparedStatement ps=con.prepareStatement("insert into Customers(name,mobile) values(?,?)");
        System.out.println("Enter User Name");
            sc.nextLine();
            String name=sc.nextLine();
        System.out.println("Enter User phone Number");
        long number=0;
        boolean flag=true;
            while(flag){
                number=sc.nextLong();
                if(Long.toString(number).length()!=10){
                    System.out.println("Invalid Mobile Number Enter Again");
                }
                else{
                    flag=false;
                }
            }

            String num=Long.toString(number);
            ps.setString(1,name);
            ps.setString(2,num);
            int rowAffected=ps.executeUpdate();
            ResultSet rs=con.createStatement().executeQuery("select user_id from Customers where mobile="+num);
        if(rowAffected>0){
            System.out.println("Added Successfully");
            System.out.print("Your ID is: ");
            
            while(rs.next()){
                System.out.println(rs.getInt(1));
                System.out.println();
                id=rs.getInt(1);
            }
            return id;
        }
        else{
            System.out.println("Failed to Add User"); 
            return 0;
        }
    }
}