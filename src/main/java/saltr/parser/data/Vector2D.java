/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.data;

import java.util.ArrayList;
import java.util.List;

public class Vector2D {
    private int width;
    private int height;
    private List<Object> rawData;
    private Vector2DIterator iterator;

    public Vector2D(int width, int height) {
        this.width = width;
        this.height = height;
        allocate();
    }

    private void allocate() {
        rawData = new ArrayList<Object>(width * height);
    }

    public void insert(int row, int col, Object object) {
        rawData.set((col * width) + row, object);
    }

    public Object retrieve(int row, int col) {
        return rawData.get((col * width) + row);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2DIterator getIterator() {
        if (iterator != null) {
            iterator = new Vector2DIterator(this);
        }
        return iterator;
    }

    List<Object> getRawData() {
        return rawData;
    }
}
