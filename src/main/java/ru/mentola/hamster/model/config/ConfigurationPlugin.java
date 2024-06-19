package ru.mentola.hamster.model.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mentola.annotatedconfigmodel.api.AbstractConfigModel;
import ru.mentola.annotatedconfigmodel.api.ConfigurationPath;

@AllArgsConstructor @Getter
public final class ConfigurationPlugin extends AbstractConfigModel {
    @ConfigurationPath(path = "max-taps")
    private final int maxTaps;
    @ConfigurationPath(path = "max-level")
    private final int maxLevel;
    @ConfigurationPath(path = "messages.no-find-extension-command")
    private final String noFindExtensionMessage;
    @ConfigurationPath(path = "messages.hamster-command.leader-board.invalid-sort-type")
    private final String invalidSortTypeLeaderBoardMessage;
    @ConfigurationPath(path = "messages.hamster-command.leader-board.invalid-usage")
    private final String invalidUsageLeaderBoardMessage;
    @ConfigurationPath(path = "messages.hamster-command.leader-board.header-board")
    private final String headerLeaderBoardMessage;
    @ConfigurationPath(path = "messages.hamster-command.leader-board.leader-row")
    private final String rowLeaderBoardMessage;
    @ConfigurationPath(path = "messages.hamster-command.open-gui.no-registered")
    private final String noRegisteredAttemptOpenGuiMessage;
    @ConfigurationPath(path = "messages.hamster-command.register.success")
    private final String successRegisterMessage;
    @ConfigurationPath(path = "messages.hamster-command..register.already")
    private final String alreadyRegisteredMessage;
    @ConfigurationPath(path = "messages.hamster-command.register.invalid-referal-code")
    private final String invalidReferalCodeMessage;
    @ConfigurationPath(path = "messages.hamster-command.register.ambassador-success")
    private final String ambassadorSuccessMessage;
    @ConfigurationPath(path = "messages.gui.header")
    private final String hamsterGuiHeaderMessage;
    @ConfigurationPath(path = "messages.gui.referal-code")
    private final String referalCode;
    @ConfigurationPath(path = "messages.gui.click-wool-green")
    private final String clickWoolGreen;
    @ConfigurationPath(path = "messages.gui.click-wool-red")
    private final String clickWoolRed;
    @ConfigurationPath(path = "messages.gui.statistic-header")
    private final String statHeader;
    @ConfigurationPath(path = "messages.gui.statistic-taps")
    private final String statTaps;
    @ConfigurationPath(path = "messages.gui.statistic-coins")
    private final String statCoins;
    @ConfigurationPath(path = "messages.gui.statistic-referals")
    private final String statReferals;
    @ConfigurationPath(path = "messages.gui.statistic-level")
    private final String statLevel;
    @ConfigurationPath(path = "messages.gui.statistic-need-to-next-level")
    private final String statNeedToNextLevel;
}
