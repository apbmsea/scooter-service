package brickboy.Infrastructure.Entity.v1.Logi;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.UUID;
@Entity
@Table(name = "event_data_logs")
public class EventDataLogi {
    @Id
    private UUID id;

    @Column(nullable = false, length = 400, unique = true)
    private String name;

    @Column(nullable = false)
    private String header;

    @Column( name="body" ,nullable = false, length = 4000)
    private String body;

    @Column(nullable = false)
    private UUID id_temlate;

    @Column(nullable = false)
    private UUID creator;

    @Column(name = "registration_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date dateCreated;

    @Column(nullable = true)
    private String patchToImage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UUID getId_temlate() {
        return id_temlate;
    }

    public void setId_temlate(UUID id_temlate) {
        this.id_temlate = id_temlate;
    }

    public UUID getCreator() {
        return creator;
    }

    public void setCreator(UUID creator) {
        this.creator = creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPatchToImage() {
        return patchToImage;
    }

    public void setPatchToImage(String patchToImage) {
        this.patchToImage = patchToImage;
    }
}
