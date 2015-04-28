package dz.sbenkhaoua.app.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sbenkhaoua on 28/04/15.
 */
@Table(name="road")
@XmlRootElement
public class RoadMapper {
    @Id
    @Column(name="id")
    private String id;
    @Column(name = "inser_date")
    private String insertDate;
    @Column(name = "source_side_x")
    private String sourceSideX;
    @Column(name = "target_side_x")
    private String targetSideX;
    @Column(name = "source_side_y")
    private String sourceSideY;
    @Column(name = "target_side_y")
    private String targetSideY;
    @Column(name = "intersection_source_id")
    private String inetrsectionSourceId;
    @Column(name = "intersection_target_id")
    private String inetrsectionTragetId;
    @Column(name = "send_date")
    private String sendDate;
    @Column(name = "road_name")
    private String roadName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getInetrsectionTragetId() {
        return inetrsectionTragetId;
    }

    public void setInetrsectionTragetId(String inetrsectionTragetId) {
        this.inetrsectionTragetId = inetrsectionTragetId;
    }

    public String getInetrsectionSourceId() {
        return inetrsectionSourceId;
    }

    public void setInetrsectionSourceId(String inetrsectionSourceId) {
        this.inetrsectionSourceId = inetrsectionSourceId;
    }


    public String getSourceSideX() {
        return sourceSideX;
    }

    public void setSourceSideX(String sourceSideX) {
        this.sourceSideX = sourceSideX;
    }

    public String getTargetSideX() {
        return targetSideX;
    }

    public void setTargetSideX(String targetSideX) {
        this.targetSideX = targetSideX;
    }

    public String getSourceSideY() {
        return sourceSideY;
    }

    public void setSourceSideY(String sourceSideY) {
        this.sourceSideY = sourceSideY;
    }

    public String getTargetSideY() {
        return targetSideY;
    }

    public void setTargetSideY(String targetSideY) {
        this.targetSideY = targetSideY;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
