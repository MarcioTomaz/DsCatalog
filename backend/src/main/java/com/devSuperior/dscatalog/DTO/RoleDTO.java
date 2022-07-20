package com.devSuperior.dscatalog.DTO;

import com.devSuperior.dscatalog.entities.Role;

import java.io.Serializable;

public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String authority;

    public RoleDTO(){}

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDTO(Role role) {
        id = role.getId();
        authority = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleDTO roleDTO = (RoleDTO) o;

        if (id != null ? !id.equals(roleDTO.id) : roleDTO.id != null) return false;
        return authority != null ? authority.equals(roleDTO.authority) : roleDTO.authority == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        return result;
    }
}
