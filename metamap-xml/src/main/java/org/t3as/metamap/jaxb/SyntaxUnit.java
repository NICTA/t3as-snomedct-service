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
    "syntaxType",
    "lexMatch",
    "inputMatch",
    "lexCat",
    "tokens"
})
@XmlRootElement(name = "SyntaxUnit")
public class SyntaxUnit {

    @XmlElement(name = "SyntaxType", required = true)
    protected String syntaxType;
    @XmlElement(name = "LexMatch")
    protected String lexMatch;
    @XmlElement(name = "InputMatch", required = true)
    protected String inputMatch;
    @XmlElement(name = "LexCat")
    protected String lexCat;
    @XmlElement(name = "Tokens", required = true)
    protected Tokens tokens;

    /**
     * Gets the value of the syntaxType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSyntaxType() {
        return syntaxType;
    }

    /**
     * Sets the value of the syntaxType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSyntaxType(String value) {
        this.syntaxType = value;
    }

    /**
     * Gets the value of the lexMatch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLexMatch() {
        return lexMatch;
    }

    /**
     * Sets the value of the lexMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLexMatch(String value) {
        this.lexMatch = value;
    }

    /**
     * Gets the value of the inputMatch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInputMatch() {
        return inputMatch;
    }

    /**
     * Sets the value of the inputMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInputMatch(String value) {
        this.inputMatch = value;
    }

    /**
     * Gets the value of the lexCat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLexCat() {
        return lexCat;
    }

    /**
     * Sets the value of the lexCat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLexCat(String value) {
        this.lexCat = value;
    }

    /**
     * Gets the value of the tokens property.
     * 
     * @return
     *     possible object is
     *     {@link Tokens }
     *     
     */
    public Tokens getTokens() {
        return tokens;
    }

    /**
     * Sets the value of the tokens property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tokens }
     *     
     */
    public void setTokens(Tokens value) {
        this.tokens = value;
    }

}
