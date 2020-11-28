
package com.example.teammatch.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Head {

    @SerializedName("vars")
    @Expose
    private List<String> vars = null;

    public List<String> getVars() {
        return vars;
    }

    public void setVars(List<String> vars) {
        this.vars = vars;
    }

}
