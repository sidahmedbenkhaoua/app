package dz.sbenkhaoua.app.model;

import com.datastax.driver.mapping.annotations.Column;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by sbenkhaoua on 30/04/15.
 */
@Table(name = "car_road")
@XmlRootElement
public class RdaRecordMaper {
    @Id
    private String id;
    @Column(name = "num")
    private Integer num;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_record")
    private Date dateRecord;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(Date dateRecord) {
        this.dateRecord = dateRecord;
    }
}
