package utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by hughie on 15/10/23.
 */
public class VsFileUtility
{
    /**
     * Some useful GFileUtility defines.
     * */
    public static final int FILE_UNDEFINED = 0;
    public static final int FILE_EXIST = 1;
    public static final int FILE_NOTEXIST = -1;
    public static final int FILE_CREATE_SUCCEED = 2;
    public static final int FILE_CREATE_FAILED = -2;
    public static final int FILE_DELETE_SUCCEED = 3;
    public static final int FILE_DELETE_FAILED = -3;

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
    /**
     * See: public static int judgeDirectory(final String dirPath)
     * */
    public static boolean judgeDirectoryCheck(final String dirPath)
    {
        int jude = judgeDirectory(dirPath);
        if(jude == FILE_EXIST || jude == FILE_CREATE_SUCCEED)
            return true;
        return false;
    }
    /**
     * Create file(text file) with given directory path and file-name, e.g. (/sdcard0/Giant/program,P9999).
     * @param dirPath Given directory path of program-file.
     * @param proFileName File-name need to be judged under dirPath.
     * */
    public static int judgeProgramFile(final String dirPath, final String proFileName)
    {
        int ret = judgeDirectory(dirPath);
        if(ret != FILE_EXIST && ret != FILE_CREATE_SUCCEED)
            return ret;
        File file = new File(dirPath + VsFileDefinition.CHAR_SLASH + proFileName);
        try
        {
            if(file.createNewFile())
                return FILE_CREATE_SUCCEED;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return FILE_UNDEFINED;
    }
    /**
     * See: public static int judgeProgramFile(final String dirPath, final String proFileName)
     * */
    public static boolean judgeProgramFileCheck(final String dirPath, final String proFileName)
    {
        int jude = judgeProgramFile(dirPath,proFileName);
        if(jude == FILE_EXIST || jude == FILE_CREATE_SUCCEED)
            return true;
        return false;
    }
}
