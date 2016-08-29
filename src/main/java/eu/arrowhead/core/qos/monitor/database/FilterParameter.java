/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.database;

/**
 * Class used to filter maps of data.
 *
 * @author 1120681@isep.ipp.pt - Renato Ayres
 */
public class FilterParameter {

    private String name;
    private String value;

    /**
     * Creates an instance of FilterParameter with a name and a value.
     *
     * @param name name of the parameter
     * @param value value of the parameter
     */
    public FilterParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     *
     * @return current name.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return current value.
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
