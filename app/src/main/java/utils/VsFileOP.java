package utils;

import java.io.File;

/**
 * Created by hughie on 15/10/23.
 */
public class VsFileOP
{
    /**
     * Delete sub directories and files of file.
     * Delete root directory.
     * @param file : Root directory File object.*/
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
     * @param filePath : Root directory String object, absolute path. */
    public static boolean deleteFile(final String filePath)
    {
        File file = new File(filePath);
        return deleteFile(file);
    }
    /**
     * Delete sub directories and files of file.
     * Ignore root directory.
     * @param dirFile : Root directory File object. */
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
     * @param filePath : Root directory String object, absolute path. */
    public static boolean cleanDirectory(final String filePath)
    {
        File file = new File(filePath);
        return cleanDirectory(file);
    }
}
