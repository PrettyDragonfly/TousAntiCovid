package fr.camillebour.covidapp.models;

import javax.persistence.*;

@Entity
@Table(name = "location")
public class Location{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String denomination;

    @Column(nullable = false, unique = true, length = 200)
    private String address;

    @Column(unique = true, length = 100)
    private String gpsCoordinates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getAddress() { return address; }

    public void setAddress(String address){
        this.address = address;
    }

    public String getGPSCoordinates() { return gpsCoordinates; }

    public void setGPSCoordinates(String gps){
        this.gpsCoordinates = gps;
    }

}
