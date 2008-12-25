package edu.ncssm.iwp.problemserver.client;

public interface AppletXmlRpcConstants
{
    public static final int OK = 1;

    // auth errors
    public static final int ERROR_UNKNOWN_AUTHKEY = -101;
    public static final int ERROR_UNKNOWN_USER = -102;
    public static final int ERROR_INVALID_CREDENTIALS = -103;

    // path errors
    public static final int ERROR_UNKNOWN_FILE = -201;
    public static final int ERROR_UNKNOWN_DIRECTORY = -202;

    // misc
    public static final int ERROR_MISSING_PARAM = -300;
    public static final int ERROR_INVALID_XML = -301;

    // system-level errors
    public static final int ERROR_DATA_STORE = -500;
    public static final int ERROR_NULL_POINTER = -501;

    public static final String ACTION_PREFIX = "pps";
    public static final String ACTION_AUTHENTICATE = "authenticate";
    public static final String ACTION_LIST_CONTENT = "listContent";
    public static final String ACTION_LIST_FILES = "listFiles";
    public static final String ACTION_LIST_DIRECTORIES = "listDirectories";
    public static final String ACTION_GET_FILE = "getFile";
    public static final String ACTION_PUT_FILE = "putFile";
    public static final String ACTION_DELETE_FILE = "deleteFile";

    public static final String ATTR_USERNAME = "username";
    public static final String ATTR_PASSWORD = "password";

    public static final String ATTR_AUTHKEY = "authkey";
    public static final String ATTR_DIRECTORY = "directory";
    public static final String ATTR_FILENAME = "filename";
    public static final String ATTR_FILES = "files";
    public static final String ATTR_DIRECTORIES = "files";
    public static final String ATTR_DATA = "data";

    public static final String ATTR_RETURN_CODE = "return_code";
    public static final String ATTR_ERROR_MESSAGE = "error_message";
}
