import java.util.*;
import java.sql.*;
import database.DatabaseConnection;
import vehicle.Vehicle_Main;
import user.User;

class main{
    public static void main(String[] args) throws SQLException, Exception{
        
        Connection con = DatabaseConnection.getDatabaseConnection();

        if(con==null){
            System.out.println("Connection Not Established");
            return;
        }
            Scanner sc=new Scanner(System.in);
            
        int choose=0;
        new vehicle.Vehicle_Main();
            System.out.println();
            System.out.println("1. Admin");
            System.out.println("2. User");
                choose=sc.nextInt();

        switch (choose){
            case 1:

                System.out.println("Enter Password");
                boolean flag=true;
                while(flag){
                    int Password=sc.nextInt();
                    if(Password!=8320){
                        System.out.println("Invalid Password Enter Again");
                    }
                    else{
                        flag=false;
                    }
                }
                    
                    int c=0;
                System.out.println("1. Register vehicle");
                System.out.println("2. Show All Vehicle");
                System.out.println("3. Remove Vehicle");
                System.out.println("4. Show All Available Vehicle");  
                System.out.println("5. Show Rented Vehicle Sorted by return date");
                System.out.println("6. Show Rented Vehicle");
                String in=sc.next();
                c=Integer.parseInt(in);
                    
                switch (c){
                    case 1:
                    
                        Vehicle_Main.Register();
                        break;

                    case 2:
                        
                        Vehicle_Main.Show_All_Vehicle();      
                        break;
                        
                    case 3:
                        
                        Vehicle_Main.Remove();
                        break;

                    case 4:

                        Vehicle_Main.Show_Available_Vehicle();                  
                        break;

                    case 5:
                        
                        Vehicle_Main.Show_Rented_Vehicle_Sorted(); 
                        break;

                    case 6:

                        Vehicle_Main.Show_Rented_Vehicle();
                        break;

                    default:
                        System.out.println("Invalid Choice");
                        break;
                }
                break;

            case 2:
                
                Vehicle_Main.UserData();
                new User();
                int id=0;

                do {
                    id=User.StartUser();
                } while (id==0);
                

                System.out.println("1. Rent");
                System.out.println("2. Return");
                int choice=sc.nextInt();

                switch (choice){
                    case 1:

                        Vehicle_Main.Rent(id);
                        break;

                    case 2:

                        Vehicle_Main.Return(id);
                        break;

                    default:
                        System.out.println("Invalid Choice");
                        break;
                }
                break;

            default:
                System.out.println("Invalid Choice");
                break;
        }
        sc.close();
    }
}