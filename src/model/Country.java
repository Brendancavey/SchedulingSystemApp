/** Author: Brendan Thoeung | Date: 9/19/2022
 * */
package model;

import java.util.Objects;

public class Country {
    private int id;
    private String name;

    ////////////CONSTRUCTOR/////////////
    public Country(int id, String name){
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString(){
        return String.valueOf(this.id) + " | " + this.name;
    }


    //////////GETTERS AND SETTERS//////////
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
