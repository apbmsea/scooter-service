package brickboy.Infrastructure.Entity.v1;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "event_ads")
public class EventListA {
    @Id
    private UUID id;

    @Column(nullable = false, length = 400)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String budy;

    @Column(nullable = false)
    private UUID creator;

    @Temporal(TemporalType.TIMESTAMP)
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBudy() {
        return budy;
    }

    public void setBudy(String budy) {
        this.budy = budy;
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
