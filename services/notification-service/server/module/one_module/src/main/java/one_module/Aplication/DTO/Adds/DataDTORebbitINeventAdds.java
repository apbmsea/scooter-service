package one_module.Aplication.DTO.Adds;

import java.time.Instant;
import java.util.UUID;

public class DataDTORebbitINeventAdds {
    private UUID MassageRebbitID;
    private Instant tameshtamp;
    private String tupe;
    private AdditionalData data;

    public UUID getMassageRebbitID() {
        return MassageRebbitID;
    }

    public void setMassageRebbitID(UUID massageRebbitID) {
        MassageRebbitID = massageRebbitID;
    }

    public Instant getTameshtamp() {
        return tameshtamp;
    }

    public void setTameshtamp(Instant tameshtamp) {
        this.tameshtamp = tameshtamp;
    }

    public String getTupe() {
        return tupe;
    }

    public void setTupe(String tupe) {
        this.tupe = tupe;
    }

    public AdditionalData getData() {
        return data;
    }

    public void setData(AdditionalData data) {
        this.data = data;
    }
}
//список дто с инфой .....


