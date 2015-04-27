package dz.sbenkhaoua.mapper.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sbenkhaoua on 27/04/15.
 */
@Table(name = "car")
@XmlRootElement
public class CarMapper implements Serializable {
    public CarMapper(){

    }
    @Id
    private String id;
    @Column(name = "car_name")
    private String carName;
    @Column(name = "color")
    private String color;
    @Column(name = "insert_date")
    private String insertDate;
    @Column(name = "pos_x")
    private String posX;
    @Column(name = "pos_y")
    private String posY;
    @Column(name = "send_date")
    private String sendDate;
    @Column(name = "speed")
    private String speed;

//    public CarMapper(String id, String carName, String color, String insertDate, String posX, String posY, String sendDate, String speed) {
////        this.id = id;
////        this.carName = carName;
////        this.color = color;
////        this.insertDate = insertDate;
////        this.posX = posX;
////        this.posY = posY;
////        this.sendDate = sendDate;
////        this.speed = speed;
//    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
