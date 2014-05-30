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
    "phraseText",
    "syntaxUnits",
    "phraseStartPos",
    "phraseLength",
    "candidates",
    "mappings"
})
@XmlRootElement(name = "Phrase")
public class Phrase {

    @XmlElement(name = "PhraseText", required = true)
    protected String phraseText;
    @XmlElement(name = "SyntaxUnits", required = true)
    protected SyntaxUnits syntaxUnits;
    @XmlElement(name = "PhraseStartPos", required = true)
    protected String phraseStartPos;
    @XmlElement(name = "PhraseLength", required = true)
    protected String phraseLength;
    @XmlElement(name = "Candidates", required = true)
    protected Candidates candidates;
    @XmlElement(name = "Mappings", required = true)
    protected Mappings mappings;

    /**
     * Gets the value of the phraseText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhraseText() {
        return phraseText;
    }

    /**
     * Sets the value of the phraseText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhraseText(String value) {
        this.phraseText = value;
    }

    /**
     * Gets the value of the syntaxUnits property.
     * 
     * @return
     *     possible object is
     *     {@link SyntaxUnits }
     *     
     */
    public SyntaxUnits getSyntaxUnits() {
        return syntaxUnits;
    }

    /**
     * Sets the value of the syntaxUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link SyntaxUnits }
     *     
     */
    public void setSyntaxUnits(SyntaxUnits value) {
        this.syntaxUnits = value;
    }

    /**
     * Gets the value of the phraseStartPos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhraseStartPos() {
        return phraseStartPos;
    }

    /**
     * Sets the value of the phraseStartPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhraseStartPos(String value) {
        this.phraseStartPos = value;
    }

    /**
     * Gets the value of the phraseLength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhraseLength() {
        return phraseLength;
    }

    /**
     * Sets the value of the phraseLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhraseLength(String value) {
        this.phraseLength = value;
    }

    /**
     * Gets the value of the candidates property.
     * 
     * @return
     *     possible object is
     *     {@link Candidates }
     *     
     */
    public Candidates getCandidates() {
        return candidates;
    }

    /**
     * Sets the value of the candidates property.
     * 
     * @param value
     *     allowed object is
     *     {@link Candidates }
     *     
     */
    public void setCandidates(Candidates value) {
        this.candidates = value;
    }

    /**
     * Gets the value of the mappings property.
     * 
     * @return
     *     possible object is
     *     {@link Mappings }
     *     
     */
    public Mappings getMappings() {
        return mappings;
    }

    /**
     * Sets the value of the mappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mappings }
     *     
     */
    public void setMappings(Mappings value) {
        this.mappings = value;
    }

}
