/*
 * $ Id $
 * $ Source $
 * Created on Jan 10, 2005
 */
package org.opengis.go.display.primitive.store;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.opengis.util.InternationalString;

/**
 * The <code>GraphicStoreFactory</code> class/interface...
 * 
 * @author SYS Technologies
 * @author crossley
 * @version $Revision $
 */
public interface GraphicStoreFactory {

    /**
     * Well-known param key indicating service requires a username for
     * authentication
     */
    public static final Param USERNAME = 
        new Param("user", String.class, "Username for authentication");

    /**
     * Well-known param key indicating service requires a username for
     * authentication
     */
    public static final Param PASSWORD = 
        new Param("password", String.class, "Password for authentication");

    /**
     * Ask for a GraphicStore connecting to the indicated provider or service.
     * The GraphicStore may have been previously cached.
     * <p>
     * Additional hints or configuration information may be provided according
     * to the metadata indicated by getParametersInfo. This information often
     * includes security information such as username and password.
     * </p>
     * 
     * @param provider Often a URL or JDBC URL locating the serivce to connect
     *            to
     * @param params Map of hints or configuration information.
     * @return GraphicStore connected to the indicated provider or service
     * @throws IOException
     */
    GraphicStore createGraphicStore(URI provider, Map params) throws IOException;

    /**
     * Icon representing this category of datastores.
     * <p>
     * Assumed to point to a 16x16 icon?
     * </p>
     * 
     * @return the icon.
     */
    URL getIcon();

    /** Display name used to communicate this type of GraphicStore to end users */
    InternationalString getDisplayName();

    /** Descrption of this type of Datastore */
    InternationalString getDescription();

    Param[] getParametersInfo();

    /**
     * Indicates this GraphicStoreFactory communicate with the indicated
     * provider or service.
     * <p>
     * This method differs from canProcess in that additional configuration
     * information may be supplied.
     * </p>
     * 
     * @param provider
     * @param params
     * @return <code>true</code> if this factory can communicate with the
     *         provider.
     */
    boolean canProcess(URI provider, Map params);

    /**
     * Allows a GraphicStoreFactory to ensure all its preconditions are met,
     * such as the presense of required libraries.
     */
    boolean isAvailable();

    /**
     * Simple service metadata - should be replaced by ISO19119 interfaces as
     * they are made available.
     * <p>
     * Differences from geotools - no param is required. Sensible defaults
     * should always be available.
     * </p>
     * 
     * @author Jody Garnett
     */
    class Param {

        /** True if Param is required. */
        final public boolean required;

        /** Key used in Parameter map. */
        final public String key;

        /** Type of information required. */
        final public Class type;

        /** Short description (less then 40 characters). */
        final public String description;

        /** Default value, please provide one users will be so much happier */
        final public Object sample;

        public Param(final String key) {
            this(key, String.class, null);
        }

        public Param(final String key, final Class type) {
            this(key, type, null);
        }

        public Param(final String key, final Class type, final String description) {
            this(key, type, description, true);
        }

        public Param(final String key, final Class type, final String description,
                final boolean required) {
            this(key, type, description, required, null);
        }

        public Param(final String key, final Class type, final String description,
                final boolean required, final Object sample) {
            this.key = key;
            this.type = type;
            this.description = description;
            this.required = required;
            this.sample = sample;
        }

        /**
         * Utility method for implementors of canProcess.
         * <p>
         * Willing to check all known constraints and parse Strings to requred
         * value if needed.
         * </p>
         * 
         * @param map Map of parameter values
         * @return Value of the correct type from map
         * @throws IOException if parameter was of the wrong type, or a required
         *             parameter is not present
         */
        public Object lookUp(final Map map) throws IOException {
            if (!map.containsKey(key)) {
                if (required) {
                    throw new IOException("Parameter " + key + " is required:" + description);
                } else {
                    return null;
                }
            }
            Object value = map.get(key);
            if (value == null) {
                return null;
            }
            if (value instanceof String && (type != String.class)) {
                value = handle((String) value);
            }
            if (value == null) {
                return null;
            }
            if (!type.isInstance(value)) {
                throw new IOException(type.getName() + " required for parameter " + key + ": not "
                        + value.getClass().getName());
            }
            return value;
        }

        public String text(final Object value) {
            return value.toString();
        }

        public Object handle(final String text) throws IOException {
            if (text == null) {
                return null;
            }
            if (type == String.class) {
                return text;
            }
            if (text.length() == 0) {
                return null;
            }
            try {
                return parse(text);
            } catch (IOException ioException) {
                throw ioException;
            } catch (Throwable throwable) {
                throw new GraphicStoreException("Problem creating " + type.getName() + " from '"
                        + text + "'", throwable);
            }
        }

        public Object parse(final String text) throws Throwable {
            Constructor constructor;

            try {
                constructor = type.getConstructor(new Class[] {
                    String.class
                });
            } catch (SecurityException e) {
                //  type( String ) constructor is not public
                throw new IOException("Could not create " + type.getName() + " from text");
            } catch (NoSuchMethodException e) {
                // No type( String ) constructor
                throw new IOException("Could not create " + type.getName() + " from text");
            }

            try {
                return constructor.newInstance(new Object[] {
                    text,
                });
            } catch (IllegalArgumentException illegalArgumentException) {
                throw new GraphicStoreException("Could not create " + type.getName() + ": from '"
                        + text + "'", illegalArgumentException);
            } catch (InstantiationException instantiaionException) {
                throw new GraphicStoreException("Could not create " + type.getName() + ": from '"
                        + text + "'", instantiaionException);
            } catch (IllegalAccessException illegalAccessException) {
                throw new GraphicStoreException("Could not create " + type.getName() + ": from '"
                        + text + "'", illegalAccessException);
            } catch (InvocationTargetException targetException) {
                throw targetException.getCause();
            }
        }
    }
}