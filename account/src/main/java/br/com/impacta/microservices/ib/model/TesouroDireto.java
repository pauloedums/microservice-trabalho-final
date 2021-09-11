package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class TesouroDireto extends PanacheEntity {

    public int cd;
    public String nm;
    public Date mtrtyDt;
    public BigDecimal minInvstmtAmt;
    public BigDecimal untrInvstmtVal;
    public String rcvgIncm;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = InvestimentClient.class)
    
    private Set<InvestimentClient> clients = new HashSet<InvestimentClient>(0);

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

    public Set<InvestimentClient> getClients() {
        return clients;
    }

    public void setClients(Set<InvestimentClient> clients) {
        this.clients = clients;
    }

    
}
