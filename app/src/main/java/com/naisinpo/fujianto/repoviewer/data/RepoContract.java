package com.naisinpo.fujianto.repoviewer.data;

import android.provider.BaseColumns;

/**
 * Created by fujianto on 04/11/14.
 */
public class RepoContract {
    public static final class RepoUserEntry implements BaseColumns{
        public static final String TABLE_NAME = "Username";
        public static final String COLUMN_USERNAME = "github_username";
    }

    public static final class RepoEntry implements BaseColumns{
        public static final String TABLE_NAME = "Repository";
        public static final String COLUMN_USER_KEY = "github_user_id";
        public static final String COLUMN_REPO_NAME = "repository_name";
        public static final String COLUMN_REPO_URL = "repository_url";
        public static final String COLUMN_REPO_DESC = "repository_desc";
        public static final String COLUMN_REPO_LANG = "repository_lang";
    }
}
