/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $URL$
 **
 ** Copyright (C) 2004-2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.metadata.extent;

// J2SE direct dependencies
import javax.units.Unit;

// OpenGIS direct dependencies
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.VerticalDatum;

// Annotations
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Vertical domain of dataset.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-111.pdf">Abstract specification 5.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="EX_VerticalExtent", specification=ISO_19115)
public interface VerticalExtent {
    /**
     * Returns the lowest vertical extent contained in the dataset.
     */
    @UML(identifier="minimumValue", obligation=MANDATORY, specification=ISO_19115)
    double getMinimumValue();

    /**
     * Returns the highest vertical extent contained in the dataset.
     */
    @UML(identifier="maximumValue", obligation=MANDATORY, specification=ISO_19115)
    double getMaximumValue();

    /**
     * Returns the vertical units used for vertical extent information.
     * Examples: metres, feet, millimetres, hectopascals.
     * 
     * @deprecated removed from ISO_19115
     */
    @UML(identifier="unitOfMeasure", obligation=MANDATORY, specification=ISO_19115)
    Unit getUnit();

    /**
     * Provides information about the origin from which the
     * maximum and minimum elevation values are measured.
     * 
     * @deprecated changed to verticalCRS in ISO_19115
     */
    @UML(identifier="verticalDatum", obligation=MANDATORY, specification=ISO_19115)
    VerticalDatum getVerticalDatum();

    /**
     * Provides information about the vertical coordinate reference system to
     * which the maximum and minimum elevation values are measured. The CRS
     * identification includes unit of measure.
     */
    @UML(identifier="verticalCRS", obligation=MANDATORY, specification=ISO_19115)
    CoordinateReferenceSystem getVerticalCRS();
}
