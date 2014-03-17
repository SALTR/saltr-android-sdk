/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTCell implements Cloneable {
    private int x;
    private int y;
    private Object properties;
    private Boolean isBocked;
    private SLTAssetInstance assetInstance;

    public SLTCell(int x, int y) {
        this.x = x;
        this.y = y;
        isBocked = false;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public Boolean getIsBocked() {
        return isBocked;
    }

    public void setIsBocked(Boolean isBocked) {
        this.isBocked = isBocked;
    }

    public SLTAssetInstance getAssetInstance() {
        return assetInstance;
    }

    public void setAssetInstance(SLTAssetInstance assetInstance) {
        this.assetInstance = assetInstance;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public SLTCell clone() {
        SLTCell cell = new SLTCell(x, y);
        cell.setAssetInstance(assetInstance);
        cell.setProperties(properties);
        cell.setIsBocked(isBocked);
        return cell;
    }
}
