/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $URL$
 **
 ** Copyright (C) 2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.coverage;

// OpenGIS dependencies
import org.opengis.spatialschema.geometry.primitive.Surface;

// Annotations
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A {@linkplain GeometryValuePair geometry-value pair} that has a {@linkplain Surface surface}
 * as the value of its geometry attribute.
 *
 * @author Alessio Fabiani
 * @author Martin Desruisseaux
 */
@UML(identifier="CV_SurfaceValuePair", specification=ISO_19123)
public interface SurfaceValuePair extends GeometryValuePair {
    /**
     * The domain object that is a member of this <var>geometry</var>-<var>value</var> pair.
     *
     * @todo According ISO-19123, the return value should be {@link Surface}. But in GeoAPI,
     *       geometry objects do not extends {@link DomainObject}...
     */
    @UML(identifier="geometry", obligation=MANDATORY, specification=ISO_19123)
    DomainObject getGeometry(); 
}