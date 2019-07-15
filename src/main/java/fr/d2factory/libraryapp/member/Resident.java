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
public class Resident extends Member{
    private String Id;
    private String fullName;

    public Resident(String Id, String fullName, float wallet) {
        super(wallet);
        this.Id = Id;
        this.fullName = fullName;
    }
    
    @Override
    public void payBook(int numberOfDays) {
        if(numberOfDays < 0){
           System.out.println("Nombre de jours negatifs");
           return ; 
        }
        float price = 0;
        if(numberOfDays <= 60){
            price = (float) (numberOfDays * 0.10); 
        }else{
            price = (float) ((60 * 0.10) + ((numberOfDays - 60) * 0.2));
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
}
