//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.03.07 at 11:12:55 AM EST 
//


package org.t3as.metamap.jaxb;

/*
 * #%L
 * NICTA t3as MetaMap XML JAXB
 * %%
 * Copyright (C) 2014 NICTA
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or combining
 * it with H2, GWT, JUnit, or JavaBeans Activation Framework (JAF) (or a
 * modified version of those libraries), containing parts covered by the
 * terms of the H2 License, the GWT Terms, the Common Public License
 * Version 1.0, or the Common Development and Distribution License (CDDL)
 * version 1.0 ,the licensors of this Program grant you additional
 * permission to convey the resulting work.
 * #L%
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "candidateScore",
    "candidateCUI",
    "candidateMatched",
    "candidatePreferred",
    "matchedWords",
    "semTypes",
    "matchMaps",
    "isHead",
    "isOverMatch",
    "sources",
    "conceptPIs",
    "status",
    "negated",
    "snomedId",
    "termType"
})
@XmlRootElement(name = "Candidate")
public class Candidate {

    @XmlElement(name = "CandidateScore", required = true)
    protected String candidateScore;
    @XmlElement(name = "CandidateCUI", required = true)
    protected String candidateCUI;
    @XmlElement(name = "CandidateMatched", required = true)
    protected String candidateMatched;
    @XmlElement(name = "CandidatePreferred", required = true)
    protected String candidatePreferred;
    @XmlElement(name = "MatchedWords", required = true)
    protected MatchedWords matchedWords;
    @XmlElement(name = "SemTypes", required = true)
    protected SemTypes semTypes;
    @XmlElement(name = "MatchMaps", required = true)
    protected MatchMaps matchMaps;
    @XmlElement(name = "IsHead", required = true)
    protected String isHead;
    @XmlElement(name = "IsOverMatch", required = true)
    protected String isOverMatch;
    @XmlElement(name = "Sources", required = true)
    protected Sources sources;
    @XmlElement(name = "ConceptPIs", required = true)
    protected ConceptPIs conceptPIs;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "Negated", required = true)
    protected String negated;

    private String snomedId;
    private String termType;

    /**
     * Gets the value of the candidateScore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCandidateScore() {
        return candidateScore;
    }

    /**
     * Sets the value of the candidateScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCandidateScore(String value) {
        this.candidateScore = value;
    }

    /**
     * Gets the value of the candidateCUI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCandidateCUI() {
        return candidateCUI;
    }

    /**
     * Sets the value of the candidateCUI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCandidateCUI(String value) {
        this.candidateCUI = value;
    }

    /**
     * Gets the value of the candidateMatched property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCandidateMatched() {
        return candidateMatched;
    }

    /**
     * Sets the value of the candidateMatched property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCandidateMatched(String value) {
        this.candidateMatched = value;
    }

    /**
     * Gets the value of the candidatePreferred property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCandidatePreferred() {
        return candidatePreferred;
    }

    /**
     * Sets the value of the candidatePreferred property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCandidatePreferred(String value) {
        this.candidatePreferred = value;
    }

    /**
     * Gets the value of the matchedWords property.
     * 
     * @return
     *     possible object is
     *     {@link MatchedWords }
     *     
     */
    public MatchedWords getMatchedWords() {
        return matchedWords;
    }

    /**
     * Sets the value of the matchedWords property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchedWords }
     *     
     */
    public void setMatchedWords(MatchedWords value) {
        this.matchedWords = value;
    }

    /**
     * Gets the value of the semTypes property.
     * 
     * @return
     *     possible object is
     *     {@link SemTypes }
     *     
     */
    public SemTypes getSemTypes() {
        return semTypes;
    }

    /**
     * Sets the value of the semTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link SemTypes }
     *     
     */
    public void setSemTypes(SemTypes value) {
        this.semTypes = value;
    }

    /**
     * Gets the value of the matchMaps property.
     * 
     * @return
     *     possible object is
     *     {@link MatchMaps }
     *     
     */
    public MatchMaps getMatchMaps() {
        return matchMaps;
    }

    /**
     * Sets the value of the matchMaps property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchMaps }
     *     
     */
    public void setMatchMaps(MatchMaps value) {
        this.matchMaps = value;
    }

    /**
     * Gets the value of the isHead property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsHead() {
        return isHead;
    }

    /**
     * Sets the value of the isHead property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsHead(String value) {
        this.isHead = value;
    }

    /**
     * Gets the value of the isOverMatch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsOverMatch() {
        return isOverMatch;
    }

    /**
     * Sets the value of the isOverMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsOverMatch(String value) {
        this.isOverMatch = value;
    }

    /**
     * Gets the value of the sources property.
     * 
     * @return
     *     possible object is
     *     {@link Sources }
     *     
     */
    public Sources getSources() {
        return sources;
    }

    /**
     * Sets the value of the sources property.
     * 
     * @param value
     *     allowed object is
     *     {@link Sources }
     *     
     */
    public void setSources(Sources value) {
        this.sources = value;
    }

    /**
     * Gets the value of the conceptPIs property.
     * 
     * @return
     *     possible object is
     *     {@link ConceptPIs }
     *     
     */
    public ConceptPIs getConceptPIs() {
        return conceptPIs;
    }

    /**
     * Sets the value of the conceptPIs property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConceptPIs }
     *     
     */
    public void setConceptPIs(ConceptPIs value) {
        this.conceptPIs = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the negated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNegated() {
        return negated;
    }

    /**
     * Sets the value of the negated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNegated(String value) {
        this.negated = value;
    }


    // ADDED PROPERTIES BELOW

    public void setSnomedId(final String snomedId) {
        this.snomedId = snomedId;
    }

    public String getSnomedId() {
        return snomedId;
    }

    public void setTermType(final String termType) {
        this.termType = termType;
    }

    public String getTermType() {
        return termType;
    }
}
