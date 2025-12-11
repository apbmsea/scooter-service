package one_module.Aplication.DTO.Users;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class DataDTORebbitINusersData {
    private UUID massageID;
    private Instant tameshtamp;
    private String tupe;
    private List<AdditionalUsersData> datas;

    public UUID getMassageID() {
        return massageID;
    }

    public void setMassageID(UUID massageID) {
        this.massageID = massageID;
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

    public List<AdditionalUsersData> getDatas() {
        return datas;
    }

    public void setDatas(List<AdditionalUsersData> datas) {
        this.datas = datas;
    }
}
