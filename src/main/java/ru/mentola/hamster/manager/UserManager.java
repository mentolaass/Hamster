package ru.mentola.hamster.manager;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.manager.api.Manager;
import ru.mentola.hamster.model.HamsterUser;
import ru.mentola.hamster.pool.ConnectionSqlPool;
import ru.mentola.hamster.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class UserManager implements Manager<HamsterUser, Player> {
    private ConnectionSqlPool pool;

    public UserManager() throws Exception {
        final Hamster plugin = Hamster.getInstance();

        if (plugin == null)
            throw new Exception();

        pool = new ConnectionSqlPool(plugin.getMySqlConfig());
    }

    @Override
    public void init() {
        if (pool == null)
            return;

        pool.connect();

        try (final Connection connection = pool.getConnection()) {
            if (connection == null)
                throw new NullPointerException("Connection is null");

            try (final PreparedStatement ps = connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS `users` (
                    `uniqueId` BIGINT PRIMARY KEY NOT NULL,
                    `username` TEXT NOT NULL,
                    `totalCountCoins` BIGINT NOT NULL,
                    `totalCountTaps` BIGINT NOT NULL,
                    `level` INT NOT NULL,
                    `referals` INT NOT NULL
                );
            """)) {
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
                if (pool != null) {
                    pool.disconnect();
                    pool = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (pool != null) {
                pool.disconnect();
                pool = null;
            }
        }
    }

    @Override @Nullable
    public HamsterUser get(Player player) {
        if (pool == null)
            return null;

        try (final Connection connection = pool.getConnection()) {
            if (connection == null)
                throw new NullPointerException("Connection is null");

            try (final PreparedStatement ps = connection.prepareStatement("""
                SELECT * FROM `users`
                WHERE `uniqueId` = ?
            """)) {
                ps.setInt(1, Util.getPlayerIntUniqueId(player));
                final ResultSet result = ps.executeQuery();
                if (result.next())
                    return new HamsterUser(
                            result.getInt("uniqueId"), result.getString("username"), result.getInt("totalCountCoins"),
                            result.getInt("totalCountTaps"), result.getInt("level"), result.getInt("referals"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void sync(HamsterUser hamsterUser) {
        if (pool == null)
            return;

        try (final Connection connection = pool.getConnection()) {
            if (connection == null)
                throw new NullPointerException("Connection is null");

            try (final PreparedStatement ps = connection.prepareStatement("""
                UPDATE `users` SET `totalCountCoins` = ?, `totalCountTaps` = ?, `level` = ?, `referals` = ?
                WHERE `uniqueId` = ?
            """)) {
                ps.setInt(1, hamsterUser.getTotalCountCoins());
                ps.setInt(2, hamsterUser.getTotalCountTaps());
                ps.setInt(3, hamsterUser.getLevel());
                ps.setInt(4, hamsterUser.getReferals());
                ps.setInt(5, hamsterUser.getUniqueId());
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(HamsterUser hamsterUser) {
        if (pool == null)
            return;

        try (final Connection connection = pool.getConnection()) {
            if (connection == null)
                throw new NullPointerException("Connection is null");

            try (final PreparedStatement ps = connection.prepareStatement("""
                INSERT INTO `users` (`uniqueId`,`username`,`totalCountCoins`,`totalCountTaps`,`level`,`referals`)
                VALUES (?, ?, ?, ?, ?, ?)
            """)) {
                ps.setInt(1, hamsterUser.getUniqueId());
                ps.setString(2, hamsterUser.getUsername());
                ps.setInt(3, hamsterUser.getTotalCountCoins());
                ps.setInt(4, hamsterUser.getTotalCountTaps());
                ps.setInt(5, hamsterUser.getLevel());
                ps.setInt(6, hamsterUser.getReferals());
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(HamsterUser hamsterUser) {
        if (pool == null)
            return;
    }

    @Override
    public List<HamsterUser> getAll() {
        try (final Connection connection = pool.getConnection()) {
            if (connection == null)
                throw new NullPointerException("Connection is null");

            try (final PreparedStatement ps = connection.prepareStatement("""
                SELECT * FROM `users`
            """)) {
                final List<HamsterUser> hamsterUsers = new ArrayList<>();
                final ResultSet result = ps.executeQuery();
                while (result.next())
                    hamsterUsers.add(new HamsterUser(
                            result.getInt("uniqueId"), result.getString("username"), result.getInt("totalCountCoins"),
                            result.getInt("totalCountTaps"), result.getInt("level"), result.getInt("referals")));
                return hamsterUsers;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public void close() {
        if (pool != null)
            pool.disconnect();
    }
}