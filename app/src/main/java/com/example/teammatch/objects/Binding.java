
package com.example.teammatch.objects;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Binding {
    public final static String NOMBRE = "nombre";
    public final static String CIUDAD = "ciudad";
    public final static String CALLE = "calle";
    public final static String LONGITUD = "longitud";
    public final static String LATITUD = "latitud";

    @SerializedName("uri")
    @Expose
    private Uri uri;
    @SerializedName("om_perteneceAInstalacionDeportiva")
    @Expose
    private OmPerteneceAInstalacionDeportiva omPerteneceAInstalacionDeportiva;
    @SerializedName("om_outdoor")
    @Expose
    private OmOutdoor omOutdoor;
    @SerializedName("tipoEspacioDeportivo")
    @Expose
    private TipoEspacioDeportivo tipoEspacioDeportivo;
    @SerializedName("geo_long")
    @Expose
    private GeoLong geoLong;
    @SerializedName("foaf_name")
    @Expose
    private FoafName foafName;
    @SerializedName("geo_lat")
    @Expose
    private GeoLat geoLat;
    @SerializedName("schema_address_addressCountry")
    @Expose
    private SchemaAddressAddressCountry schemaAddressAddressCountry;
    @SerializedName("schema_address_addressLocality")
    @Expose
    private SchemaAddressAddressLocality schemaAddressAddressLocality;
    @SerializedName("schema_address_streetAddress")
    @Expose
    private SchemaAddressStreetAddress schemaAddressStreetAddress;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public OmPerteneceAInstalacionDeportiva getOmPerteneceAInstalacionDeportiva() {
        return omPerteneceAInstalacionDeportiva;
    }

    public void setOmPerteneceAInstalacionDeportiva(OmPerteneceAInstalacionDeportiva omPerteneceAInstalacionDeportiva) {
        this.omPerteneceAInstalacionDeportiva = omPerteneceAInstalacionDeportiva;
    }

    public OmOutdoor getOmOutdoor() {
        return omOutdoor;
    }

    public void setOmOutdoor(OmOutdoor omOutdoor) {
        this.omOutdoor = omOutdoor;
    }

    public TipoEspacioDeportivo getTipoEspacioDeportivo() {
        return tipoEspacioDeportivo;
    }

    public void setTipoEspacioDeportivo(TipoEspacioDeportivo tipoEspacioDeportivo) {
        this.tipoEspacioDeportivo = tipoEspacioDeportivo;
    }

    public GeoLong getGeoLong() {
        return geoLong;
    }

    public void setGeoLong(GeoLong geoLong) {
        this.geoLong = geoLong;
    }

    public FoafName getFoafName() {
        return foafName;
    }

    public void setFoafName(FoafName foafName) {
        this.foafName = foafName;
    }

    public GeoLat getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(GeoLat geoLat) {
        this.geoLat = geoLat;
    }

    public SchemaAddressAddressCountry getSchemaAddressAddressCountry() {
        return schemaAddressAddressCountry;
    }

    public void setSchemaAddressAddressCountry(SchemaAddressAddressCountry schemaAddressAddressCountry) {
        this.schemaAddressAddressCountry = schemaAddressAddressCountry;
    }

    public SchemaAddressAddressLocality getSchemaAddressAddressLocality() {
        return schemaAddressAddressLocality;
    }

    public void setSchemaAddressAddressLocality(SchemaAddressAddressLocality schemaAddressAddressLocality) {
        this.schemaAddressAddressLocality = schemaAddressAddressLocality;
    }

    public SchemaAddressStreetAddress getSchemaAddressStreetAddress() {
        return schemaAddressStreetAddress;
    }

    public void setSchemaAddressStreetAddress(SchemaAddressStreetAddress schemaAddressStreetAddress) {
        this.schemaAddressStreetAddress = schemaAddressStreetAddress;
    }

    public static void packageIntent(Intent intent, Binding b){
        intent.putExtra(Binding.NOMBRE, b.getFoafName().getValue());
        intent.putExtra(Binding.CIUDAD, b.getSchemaAddressAddressLocality().getValue());
        if( b.getSchemaAddressStreetAddress() == null)
            intent.putExtra(Binding.CALLE,"No disponible");
        else
            intent.putExtra(Binding.CALLE, b.getSchemaAddressStreetAddress().getValue());
        intent.putExtra(Binding.LONGITUD, b.getGeoLong().getValue());
        intent.putExtra(Binding.LATITUD, b.getGeoLat().getValue());
    }
}
