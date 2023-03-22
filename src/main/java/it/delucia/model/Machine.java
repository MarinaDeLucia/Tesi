package it.delucia.model;

public class Machine {
    private int id;

    public Machine(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMachineName(){
        return "M" + id;
    }

    //toString method
    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                '}';
    }
}
