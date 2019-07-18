/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author shiva
 */
public class Main {

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
    Main(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "123456789s");
            System.out.println("Connection Established....");
            /*
            rs = st.executeQuery("Select * from student");
            System.out.println("EID\tENAME\tESAL");
            System.out.println("------------------------------------------");
            while(rs.next()){
                System.out.print(rs.getInt(1)+"\t");
                System.out.print(rs.getString(2)+"\t");
                System.out.print(rs.getString(3)+"\n");    

            }
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closedb(){
            try {
                con.close();
                System.out.println("Connection Closed! successfully");
                //return(true);
            } catch (Exception e) {
                e.printStackTrace();
                //return(false);               
                System.out.println("problem closing Connection!");

            }
        }
    
    public Boolean logincheck(String id, String pass){      //called from login page at click of login button;
        String xxx = "";
        try{
            st = con.prepareStatement("SELECT * FROM teacher WHERE teacher_id = ?");
            st.setString(1, id);
            rs = st.executeQuery();
            rs.next();
            xxx = rs.getString(4);
            System.out.println("Working good here");
                rs.close();
                st.close();
        }
        catch(Exception e){
            System.out.println("Exception in logincheck-method!");
            e.printStackTrace();
        }
            if(pass.equals(xxx)){
                System.out.println("Login Successful!");
                return(true);
            }
            else
                return(false);
    }
  
    public String getInstuctorName(String id){
    String xxx = null;
        try{
        st = con.prepareStatement("SELECT * FROM teacher WHERE teacher_id = ?");
        st.setString(1,id);
        rs = st.executeQuery();
        rs.next();
        xxx = rs.getString(2);
                rs.close();
                st.close();
        }catch(Exception e){            
        System.out.println("Exception in getInstructorName-method!");
        e.printStackTrace();
    }
        return(xxx);
    }
   
    public void registerAttendence(String courseid, String rollno, String date, String status){
        try{
            String query = "Insert into attendence values(?,?,?,?)";
            PreparedStatement psmt  = con.prepareStatement(query);
            psmt.setString(1, rollno);
            psmt.setString(2, date);            
            psmt.setString(3, courseid);
            psmt.setString(4, status);  //password for instrctor is same as ID;
            psmt.execute();
                rs.close();
                psmt.close();
        }catch(Exception e){
            System.out.println("Exception in registerAttendence-method!");
            e.printStackTrace();
        }
    }
    
    public String getAttendence(String cid,String rollno,String from,String to){
        String feedback = "Name:";
        try{
            PreparedStatement psmt  = con.prepareStatement("select count(*) from attendence where rollno = ? and (date >= ? and date <= ?) and sub_id = ? and present = ?");
            psmt.setInt(1, Integer.parseInt(rollno));
            psmt.setString(2, from);            
            psmt.setString(3, to);
            psmt.setString(4, cid);  
            psmt.setString(5,"true");
            rs = psmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            //////////////////////////////////
            PreparedStatement psmt1  = con.prepareStatement("select count(distinct(date)) from attendence where sub_id = ? and (date <=? and date >= ?)");
            //OR :: select count(*) from attendence where rollno = ? and (date >= ? and date <= ?) and course_id = ? and (present = 'true' or present = 'false')
            psmt1.setString(1, cid);            
            psmt1.setString(2, to);
            psmt1.setString(3, from);  
            rs = psmt1.executeQuery();
            rs.next();
            int all = rs.getInt(1);
            ////////////////////////////////////
            st = con.prepareStatement("SELECT * FROM student WHERE rollno = ?");
            st.setInt(1,Integer.parseInt(rollno));
            rs = st.executeQuery();
            rs.next();
            String name = rs.getString(2);
            feedback = feedback+name+":: "+count+"/"+all;
                rs.close();
                psmt1.close();
                psmt.close();
        }catch(Exception e){
            System.out.println("Exception in getAttendence-method!");
            e.printStackTrace();
        }
        System.out.println("Returning name and count!");
        return(feedback);
    }
    public String admin_addInstuctor(String Name, String Id, String CourseId){
        String feedback = "Name:";
        try{
            String query = "Insert into teacher values(?,?,?,?)";
            PreparedStatement psmt  = con.prepareStatement(query);
            psmt.setString(1, Id);
            psmt.setString(2, Name);            
            psmt.setString(3, CourseId);
            psmt.setString(4, Id);  //password for instrctor is same as ID;
            if (psmt.execute())
                System.out.println("New tupple Inserted!");
            else
                System.out.println("Problem Inserting values in teacher table!");

                rs.close();
                psmt.close();
        }catch(Exception e){
            System.out.println("Exception in admin_addInstuctor-method!");
            e.printStackTrace();
        }
        System.out.println("Successfully inserted new instuctor");
        return(feedback);
    }

    public String admin_removeInstuctor(String Id){
        String feedback = "Row Affected due to last operation:";
        try{
            String query = "DELETE from teacher WHERE teacher_id = ?";
            PreparedStatement psmt  = con.prepareStatement(query);
            psmt.setString(1, Id);
            feedback += psmt.execute();
                rs.close();
                psmt.close();
        }catch(Exception e){
            System.out.println("Exception in admin_removeInstructor-method!");
            e.printStackTrace();
        }
        System.out.println("Successfully Removed old instuctor");
        return(feedback);
    }
    
    public String admin_addCourse(String id,String name){
        String feedback = "Row Affected due to last operation:";
        try{
            String query = "INSERT INTO course values(?, ?)";
            PreparedStatement psmt  = con.prepareStatement(query);
            psmt.setString(1, id);
            psmt.setString(1, name);
            feedback += psmt.execute();
        }catch(Exception e){
            System.out.println("Exception in admin_addcourse-method!");
            e.printStackTrace();
        }
        System.out.println("Successfully added new course");
        return(feedback);
    }
    
        public String admin_removeCourse(String id){
            String feedback = "Row Affected due to last operation:";
        try{
            String query = "DELETE FROM course WHERE course_id = ?";
            PreparedStatement psmt  = con.prepareStatement(query);
            psmt.setString(1, id);
            feedback = feedback+psmt.execute();
        }catch(Exception e){
            System.out.println("Exception in admin_addcourse-method!");
            e.printStackTrace();
        }
        System.out.println("END of admin_addcourse-method!");
        return(feedback);
    }
    
    public static void main(String[] args){
        //Don't Know how to use You.
    }
    
}
