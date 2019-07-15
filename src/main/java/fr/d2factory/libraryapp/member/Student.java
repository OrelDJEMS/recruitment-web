/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.d2factory.libraryapp.member;

/**
 *
 * @author DJEMS
 */
public class Student extends Member{
    private String Id;
    private String fullName;
    private int yearStudent;

    public Student(String Id, String fullName, int yearStudent, float wallet) {
        super(wallet);
        this.Id = Id;
        this.fullName = fullName;
        this.yearStudent = yearStudent;
    }
    
    @Override
    public void payBook(int numberOfDays) {
        if(numberOfDays < 0){
           System.out.println("Nombre de jours negatifs");
           return ; 
        }
        float price = 0;
        if(this.getYearStudent() == 1){
            if(numberOfDays <= 15){
                //do nothing
            }else if(numberOfDays > 15 && numberOfDays <= 30){
               price = (float) ((numberOfDays - 15) * 0.10);
            }else{
               price = (float) ((15 * 0.10) + ((numberOfDays - 30) * 0.15));
            }
        }else{
          if(numberOfDays <= 30){
              price = (float) (numberOfDays * 0.10); 
          }else{
              price = (float) ((30 * 0.10) + ((numberOfDays - 30) * 0.15));
          }  
        }
        if((this.getWallet() >= price)){
            this.setWallet(this.getWallet() - price);
        }else{
            System.out.println("solde insuffisant");
        }
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearStudent() {
        return yearStudent;
    }

    public void setYearStudent(int yearStudent) {
        this.yearStudent = yearStudent;
    }
}
