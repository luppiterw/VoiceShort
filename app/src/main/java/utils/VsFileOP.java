package utils;

import java.io.File;

/**
 * Created by hughie on 15/10/23.
 */
public class VsFileOP
{
    /**
     * Some useful GFileUtility defines.
     * */
    public static int FILE_UNDEFINED = 0;
    public static int FILE_EXIST = 1;
    public static int FILE_NOTEXIST = -1;
    public static int FILE_CREATE_SUCCEED = 2;
    public static int FILE_CREATE_FAILED = -2;
    public static int FILE_DELETE_SUCCEED = 3;
    public static int FILE_DELETE_FAILED = -3;

    /**
     * Delete sub directories and files of file.
     * Delete root directory.
     * @param file  Root directory File object.
     * */
    public static boolean deleteFile(final File file)
    {
        if(file.isFile())
            return file.delete();
        else if(file.isDirectory())
        {
            File[] subFiles = file.listFiles();
            if(subFiles == null || subFiles.length == 0)
                return file.delete();

            for(File f : subFiles)
                if( !deleteFile(f) )  ///< need to deal with ?
                    return false;

            return file.delete();
        }
        return false;
    }
    /**
     * Delete sub directories and files of file.
     * Delete root directory.
     * @param filePath  Root directory String object, absolute path.
     * */
    public static boolean deleteFile(final String filePath)
    {
        File file = new File(filePath);
        return deleteFile(file);
    }
    /**
     * Delete sub directories and files of file.
     * Ignore root directory.
     * @param dirFile : Root directory File object.
     * */
    public static boolean cleanDirectory(final File dirFile)
    {
        if(!dirFile.isDirectory())
            return false;
        File[] files = dirFile.listFiles();
        for(File f : files)
            if( !deleteFile(f) )
                return false;
        return false;
    }
    /**
     * Delete sub directories and files of file.
     * Ignore root directory.
     * @param filePath  Root directory String object, absolute path.
     * */
    public static boolean cleanDirectory(final String filePath)
    {
        File file = new File(filePath);
        return cleanDirectory(file);
    }

    /**
     * Create directory with given path, e.g. /sdcard0/Giant/program.
     * If this directory exists, it'll do nothing.
     * If dirPath exists and is not a directory, it'll delete and create a new one.
     * If dirPath doesn't exist, it'll create the directory right now.
     * @param dirPath Given path of directory.
     * @return GF_VALUES:
     * */
    public static int judgeDirectory(final String dirPath)
    {
        File file = new File(dirPath);
        if(file.exists())
        {
            if(file.isDirectory())
                return FILE_EXIST;
            else
            {
                if(!file.delete())
                    return FILE_DELETE_FAILED;
            }
        }

        if(!file.mkdirs())
            return FILE_CREATE_FAILED;

        return FILE_CREATE_SUCCEED;
    }
}
