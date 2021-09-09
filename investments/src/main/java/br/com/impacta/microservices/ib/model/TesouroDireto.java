package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "tesouroDireto")
public class TesouroDireto extends PanacheEntity {

    public int cd;
    public String nm;
    public Date mtrtyDt;
    public BigDecimal minInvstmtAmt;
    public BigDecimal untrInvstmtVal;
    public String rcvgIncm;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Client.class)
    @JsonbTransient
    private Set<Client> clients = new HashSet<Client>(0);

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public Date getMtrtyDt() {
        return mtrtyDt;
    }

    public void setMtrtyDt(Date mtrtyDt) {
        this.mtrtyDt = mtrtyDt;
    }

    public BigDecimal getMinInvstmtAmt() {
        return minInvstmtAmt;
    }

    public void setMinInvstmtAmt(BigDecimal minInvstmtAmt) {
        this.minInvstmtAmt = minInvstmtAmt;
    }

    public BigDecimal getUntrInvstmtVal() {
        return untrInvstmtVal;
    }

    public void setUntrInvstmtVal(BigDecimal untrInvstmtVal) {
        this.untrInvstmtVal = untrInvstmtVal;
    }

    public String getRcvgIncm() {
        return rcvgIncm;
    }

    public void setRcvgIncm(String rcvgIncm) {
        this.rcvgIncm = rcvgIncm;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    
}
