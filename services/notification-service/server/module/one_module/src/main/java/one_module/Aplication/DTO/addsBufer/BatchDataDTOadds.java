package one_module.Aplication.DTO.addsBufer;

import one_module.Aplication.DTO.Adds.DataDTORebbitINeventAdds;

import java.util.List;

public class BatchDataDTOadds {
    private List<DataDTORebbitINeventAdds> eventAdds;

    public BatchDataDTOadds(List<DataDTORebbitINeventAdds> eventAdds) {
        this.eventAdds = eventAdds;
    }

    public List<DataDTORebbitINeventAdds> getEventAdds() {
        return eventAdds;
    }

    public void setEventAdds(List<DataDTORebbitINeventAdds> eventAdds) {
        this.eventAdds = eventAdds;
    }
}
