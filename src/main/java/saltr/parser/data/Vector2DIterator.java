/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.data;

public class Vector2DIterator {
    private Vector2D vector2D;
    private int vectorLength;
    private int currentPosition;

    public Vector2DIterator(Vector2D vector2D) {
        this.vector2D = vector2D;
        reset();
    }

    public Boolean hasNext() {
        return currentPosition != vectorLength;
    }

    public Object next() {
        return vector2D.getRawData().get(currentPosition++);
    }

    public void reset() {
        vectorLength = vector2D.getRawData().size();
        currentPosition = 0;
    }
}
