package one_module.Aplication.DTO.Adds;

import java.util.List;
import java.util.UUID;

public class AdditionalData {

    public UUID id_add_data;
    public UUID id_add_template;
    public List<Segments> segmentList;

    public UUID getId_add_data() {
        return id_add_data;
    }

    public void setId_add_data(UUID id_add_data) {
        this.id_add_data = id_add_data;
    }

    public UUID getId_add_template() {
        return id_add_template;
    }

    public void setId_add_template(UUID id_add_template) {
        this.id_add_template = id_add_template;
    }

    public List<Segments> getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(List<Segments> segmentList) {
        this.segmentList = segmentList;
    }
}
