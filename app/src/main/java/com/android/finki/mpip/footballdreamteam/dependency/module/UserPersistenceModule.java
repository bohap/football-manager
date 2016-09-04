package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.content.Context;

import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.database.repository.CommentRepository;
import com.android.finki.mpip.footballdreamteam.database.repository.LikeRepository;
import com.android.finki.mpip.footballdreamteam.database.repository.LineupPlayerRepository;
import com.android.finki.mpip.footballdreamteam.database.repository.LineupRepository;
import com.android.finki.mpip.footballdreamteam.database.repository.PlayerRepository;
import com.android.finki.mpip.footballdreamteam.database.repository.PositionRepository;
import com.android.finki.mpip.footballdreamteam.database.repository.TeamRepository;
import com.android.finki.mpip.footballdreamteam.database.service.CommentDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LikeDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupPlayerDBService;
import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.database.service.TeamDBService;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.UserScope;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PlayerUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 09.08.2016.
 */
@Module
public class UserPersistenceModule {

    /**
     * Provides instance of the LineupPlayerValidator.
     *
     * @return instance of LineupPlayerValidator
     */
    @Provides
    @UserScope
    LineupPlayerValidator provideLineupPlayerValidator() {
        return new LineupPlayerValidator();
    }

    /**
     * Provides instance of TeamDBService.
     *
     * @param context  base application context
     * @param dbHelper application OpenSQLiteHelper
     * @return instance of TeamDBService
     */
    @Provides
    @UserScope
    TeamDBService provideTeamDBService(Context context, MainSQLiteOpenHelper dbHelper) {
        TeamRepository repository = new TeamRepository(context, dbHelper);
        return new TeamDBService(repository);
    }

    /**
     * Provides instance of PositionDBService.
     *
     * @param context  base application context
     * @param dbHelper application OpenSQLIteHelper
     * @return instance of PositionDBService
     */
    @Provides
    @UserScope
    PositionDBService providePositionDBService(Context context, MainSQLiteOpenHelper dbHelper) {
        PositionRepository repository = new PositionRepository(context, dbHelper);
        return new PositionDBService(repository);
    }

    /**
     * Provides instance of PlayerDBService.
     *
     * @param context           base application context
     * @param dbHelper          application OpenSQLiteHelper
     * @param teamDBService     instance of TeamDBService
     * @param positionDBService instance of PositionDBService
     * @return instance of PlayerDBService
     */
    @Provides
    @UserScope
    PlayerDBService providePlayerDBService(Context context, MainSQLiteOpenHelper dbHelper,
                                           TeamDBService teamDBService, PositionDBService positionDBService) {
        PlayerRepository repository = new PlayerRepository(context, dbHelper);
        return new PlayerDBService(repository, teamDBService, positionDBService);
    }

    /**
     * Provides instance of LineupDBService.
     *
     * @param context       base application context
     * @param dbHelper      application OpenSQLiteHelper
     * @param userDBService instance of UserDBService
     * @return instance of LineupDBService
     */
    @Provides
    @UserScope
    LineupDBService provideLineupDBService(Context context, MainSQLiteOpenHelper dbHelper,
                                           UserDBService userDBService) {
        LineupRepository repository = new LineupRepository(context, dbHelper);
        return new LineupDBService(repository, userDBService);
    }

    /**
     * Provides instance of CommentDBService.
     *
     * @param context         base application context
     * @param dbHelper        application OpenSQLIteHelper
     * @param userDBService   instance of UserDBService
     * @param lineupDBService instance of LineupDBService
     * @return instance of CommentDbService
     */
    @Provides
    @UserScope
    CommentDBService provideCommentDBService(Context context, MainSQLiteOpenHelper dbHelper,
                                 UserDBService userDBService, LineupDBService lineupDBService) {
        CommentRepository repository = new CommentRepository(context, dbHelper);
        return new CommentDBService(repository, userDBService, lineupDBService);
    }

    /**
     * Provides instance of LineupPlayerDBService.
     *
     * @param context           base application context
     * @param dbHelper          application OpenSQLiteHelper
     * @param lineupDBService   instance of LineupDBService
     * @param playerDBService   instance of PlayerDBService
     * @param positionDBService instance of PositionDBService
     * @return instance of LineupPlayerDBService
     */
    @Provides
    @UserScope
    LineupPlayerDBService provideLineupPlayerDBService(Context context,
                           MainSQLiteOpenHelper dbHelper, LineupDBService lineupDBService,
                           PlayerDBService playerDBService, PositionDBService positionDBService) {
        LineupPlayerRepository repository = new LineupPlayerRepository(context, dbHelper);
        return new LineupPlayerDBService(repository, lineupDBService,
                playerDBService, positionDBService);
    }

    /**
     * Provides instance of LikeDBService.
     *
     * @param context         base application context
     * @param dbHelper        application OpenSQLiteHelper
     * @param userDBService   instnac eof UserDBService
     * @param lineupDBService instance of LineupDBService
     * @return instance of LikeDBService
     */
    @Provides
    @UserScope
    LikeDBService provideLikeDBService(Context context, MainSQLiteOpenHelper dbHelper,
                                   UserDBService userDBService, LineupDBService lineupDBService) {
        LikeRepository repository = new LikeRepository(context, dbHelper);
        return new LikeDBService(repository, userDBService, lineupDBService);
    }

    /**
     * Provides instance of the PositionUtils.
     *
     * @return instance of PositionUtils
     */
    @Provides
    @UserScope
    PositionUtils providePositionsUtils() {
        return new PositionUtils();
    }

    /**
     * Provides instance of the LineupUtils.
     *
     * @param utils instance of PositionUtils
     * @return instance of LineupUtils
     */
    @Provides
    @UserScope
    LineupUtils provideLineupUtils(PositionUtils utils) {
        return new LineupUtils(utils);
    }

    /**
     * Provides instance of the PlayerUtils.
     *
     * @return instance of PlayerUtils
     */
    @Provides
    @UserScope
    PlayerUtils providePlayerUtils() {
        return new PlayerUtils();
    }
}