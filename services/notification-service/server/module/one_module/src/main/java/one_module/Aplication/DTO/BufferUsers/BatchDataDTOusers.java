package one_module.Aplication.DTO.BufferUsers;

import one_module.Aplication.DTO.Users.DataDTORebbitINusersData;

import java.util.List;

public class BatchDataDTOusers {
    private List<DataDTORebbitINusersData> users;

    public BatchDataDTOusers(List<DataDTORebbitINusersData> users) {
        this.users = users;
    }

    public List<DataDTORebbitINusersData> getUsers() {
        return users;
    }

    public void setUsers(List<DataDTORebbitINusersData> users) {
        this.users = users;
    }
}
