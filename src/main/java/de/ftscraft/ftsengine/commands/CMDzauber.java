package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDzauber implements CommandExecutor {

    public CMDzauber(Engine plugin) {
        plugin.getCommand("zauber").setExecutor(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("zauber.zauber")) {
                if (command.getName().equalsIgnoreCase("zauber")) {
                    if (args.length == 0) {
                        player.sendMessage("§7[§5FTS-Engine§7] Bitte benutze den Command so: \n§c/zauber §8[§cZaubername§8]§7 oder §c/zauber liste§7.");
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("liste")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEine Liste der Zauber ist hier §c/zauber §8[§cMenschen§8]§b, §8[§cHochelfen§8]§b, §8[§cHochzwerge§8]§b, §8[§cEisenzwerge§8]§b, §8[§cDunkelelfen§8]§b, §8[§cGrauorks§8]§b, §8[§cGoblins§8]§7§b, §8[§cForum§8]§b");

                        } else if (args[0].equalsIgnoreCase("Menschen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bMenschen haben keine Magie aber einen Schutzwurf. Der Schutzwurf liegt bei 6 – 8 .Dies gilt für alle Zauber, auch für Heilzauber zu Gunsten des Menschen. Der Schutzwurf muss gewürfelt werden.");

                            //28
                        } else if (args[0].equalsIgnoreCase("Hochelfen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §9Rosenranken, §bHeilenderHauch, §9ToxischerHauch, §bKlängerDerNatur, §9HeilendeQuelle, §bVereisung, §9Erdrüstung, §bSteinhieb, §9SeismischerSinn, §bErdwelle, §9Feuerkugel, §bFeuersiegel, §9Feuerball, §bWindstoß, §9Blitzeinschlag, §bVakuumkuppel, §9Winschild, §bArkanerImpuls, §9ArkanerSchild, §bMeditation, §9Telephatie, §bCuilanquil, §9SchützendesLicht, §bFinsternisDurchblicken, §9BerührungDesLichts, §bFinsternisVertreiben, §9Läuterung, §bLichtverpflichteterSegen");

                        } else if (args[0].equalsIgnoreCase("Rosenranken")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt dornige Ranken aus dem Boden sprießen die das Ziel fesseln und ihm 1Lp schaden zufügen (Keine Verletzung bei Rüstung. Das Ziel muss in jeder Runde würfeln, um sich zu befreien. Dies kostet den Angriffswurf.)");

                        } else if (args[0].equalsIgnoreCase("HeilenderHauch")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bE Hauch aus heilender Magie, die einfache Wunden verschliesen und oberflächliche Verletzungen heilen kann (Heilt 1 Lp)");

                        } else if (args[0].equalsIgnoreCase("ToxischerHauch")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEin Hauch aus giftiger Magie, welche das Opfer kurzzeitig lähmt (Gegner kann eine Runde keine Aktion durchführen/ hat der Gegner 1Lp wird der Gegner 3min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("KlängeDerNatur")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEin lieblicher Melodie ertönt, alle Nicht-Elfen entweder schläfrig macht , sie beruhigt, sie ablenkt oder ihnen Mut verleiht");

                        } else if (args[0].equalsIgnoreCase("HeilendeQuelle")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEin Quell magischen Wasser, welches beim trinken Müdigkeit, Erschöpfung und Unwohl sein nimmt. Auf der Haut kann es Wunden indesifizieren und leichte Brandwunden kühlen (Heilt 1 Lp)");

                        } else if (args[0].equalsIgnoreCase("Vereisung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDas Ziel wird durch plötzlich aufkommender Kälte gelähmt (der Gegner setzt eine Runde aus + in eisigen Regionen 1Lp Schaden / wenn der Gegner 1Lp hat, wird dieser gelähmt, maximal 3 min, ausser das Ziel wird wärme ausgesetzt, was die Lähmzeit mindert (in Jungel 2min, Wüste 1min))");

                        } else if (args[0].equalsIgnoreCase("Erdrüstung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEine harte Erdschicht bildet sich auf der Haut des Anwenders, die gegen die meisten Zauber und Angriffe schützt.(gibt 1 Rp für 5min)");

                        } else if (args[0].equalsIgnoreCase("Steinhieb")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEin Stein, welcher mit magischer Kraft verhärtet und auf das Ziel geschleudert wird. (1 Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("SeismischerSinn")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender kann durch die Vibration im Boden Personen in der Umgebung wahrnehmen, wenn diese sich bewegen und ihre Worte selbst durch Mauern hören (Den Rp-Chat den man hört, ist trz Steinmauern oder anderen, schalldichten Hindernissen im Weg, völlig legitim / 5min)");

                        } else if (args[0].equalsIgnoreCase("Erdwelle")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender ist in der Lage durch den Kontakt mit dem Boden, eine Druckwelle auszulösen, die je nach dem wie der Anwender es möchte, den Gegner nur zurück schleudert, oder diese durch die Vibration der Erde für paralysiert. Es soll nicht im Haus eingesetzt werden. (Zurückschleudern / Bei Paralyse: Der Gegner setzt im Kampf eine Runde aus, hat er nur noch 1 Lp ist dieser 1min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("Feuerkugel")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEine Kugel aus Feuer, die die Umgebung beleuchtet, Feuer oder eine Pfeife entzünden.");

                        } else if (args[0].equalsIgnoreCase("Feuersiegel")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender bringt ein Siegel auf einer Tür an. Sobald jemand versucht die Tür zu öffnen wird diese unberührbar heiß. (3 min/Tür kann unmöglich geöffnet werden + 1Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("Feuerball")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEin Feuerball, der auf das Ziel zu schiest und leichte Verbrennungen verursacht (1 Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("Windstoß")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEine starke Sturmböhe die das Ziel wenige Meter weg schleudert (max. 6m / + 1Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("Blitzeeinschlag")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bBeschwört einen Blitz, der dem Ziel leichte Verbrennungen zuzieht und das getroffene Ziel lähmt (1 Lp Schaden oder der Gegner setzt eine Runde aus (muss vor dem Wirken festgelegt werden und es darf sich in jeder Runde unentschieden werden)/ hat der Gegner 1 Lp , wird dieser 3min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("Vakuumkuppel")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bKann einem Ziel kurzzeitig die Atemluft nehmen , bis dieses Bewusstlos wird (Gegner setzt eine Runde aus/ hat der Gegner 1Lp wird dieser 3min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("Windschild")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bBeschwört einen starken Wind , welcher wie ein Schild, einen physischen Angriff (auch physikalische Projektile wie Pfeile, Bolzen, Steine etc). Gegner im Nahkampf werden zurück geschleudert.");

                        } else if (args[0].equalsIgnoreCase("ArkanerImpuls")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDas Ziel wird einige Meter weg geschleudert und ist für gelähmt (Der Gegner setzt eine Runde aus/hat der Gegner 1Lp ist dieser 1min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("ArkanerSchild")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEin Schild aus purer magischer Energie , welcher ein magischen Angriff abwehrt (nur arkane oder Lichtmagie, keine Chaosmagie)");

                        } else if (args[0].equalsIgnoreCase("Meditation")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bWährend der Meditation ist der Anwender nicht in der Lage auf physische Interaktionen zu reagieren, so lange sich der Anwender im meditativen Zustand befindet. Während der Mediation spührt der Anwender keine Schmerzen und keinen Hunger oder Durst. Der Anwender ist so fokussiert, dass psychische Angriffe magischer Natur (z.b Illusionsmagie) keinen Einfluss auf den Geist des Anwenders nehmen.");

                        } else if (args[0].equalsIgnoreCase("Telephatie")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender kann eine telephatische Nachricht, an ein Ziel senden, mit welchem der Anwender Augenkontakt hat. (der Zauber kostet zwei tägliche Magieanwendungen pro Nachricht)");

                        } else if (args[0].equalsIgnoreCase("Cuilanquil")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bMan gibt sein Leben für das eines anderen, welcher kurz vor dem Sterben ist oder erst seit wenigen Stunden Tod ist. Jedoch ist das Wiederbeleben von längst verstorbenen Personen nicht möglich. (Es wird mit dem Magiewürfel gewürfelt: Bei 1, 11-12 - sterben beide. Der Wiederbelende regeneriert bei 2-4 = 1Lp; bei 5-7 = 2Lp; bei 8-10 volle Lp. Nach dem Wirken des Zaubers verstirbt der Anwender)");

                        } else if (args[0].equalsIgnoreCase("SchützendesLicht")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört ein gleißenden Schild, welcher einen Chaoszauber absorbiert.");

                        } else if (args[0].equalsIgnoreCase("FinsternisDurchblicken")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender kann einen Ilusionszauber bzw einen Zauber abwehren, der den Geist angreift oder auf den Verstand angewandt wird.");

                        } else if (args[0].equalsIgnoreCase("BerührungDesLichts")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Hände des Anwenders beginnen zu glühen und senden eine wohltuende Wärme aus. Bei Berührung schließen sich kleinere Wunden und oberflächliche Verletzungen. (Heilt 1 Lp / Chaoswesen bekommen durch die Reinheit des Lichts Schaden)");

                        } else if (args[0].equalsIgnoreCase("FinsternisVertreiben")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender erschafft in einem Umkreis von 25m² eine Fläche aus reinen Licht. In diesen Bereich ist es für ein Chaoswesen unmöglich einzutreten. (Dauer: 3min/im Kampf ist für eine Runde, dem Chaoswesen nicht möglich einen Nahkampfangriff auszuführen)");

                        } else if (args[0].equalsIgnoreCase("Läuterung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender ist in der Lage einen Fluch vom Ziel zu entfernen.");

                        } else if (args[0].equalsIgnoreCase("LichtverpflichteterSegen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender hüllt seine Waffe in helles Licht, wodurch diese bei Chaosanhängern mehr Schaden verursacht wird. (im Rp-Kampf verliert ein Chaoswesen, statt ein, gleich zwei Leben. Gerüstete Gegner verlieren 1 Rp und 1 Lp bei einem Treffer (ausgenommen bei Netherit oder Diamantrüstung)/nur der Anführer einer Hochelfen-Faction darf diesen Zauber lernen und darf nur einmal in einer Faction vorkommen/es muss erst mit dem Magiewürfel geworfen werden, um den Zauber zu aktivieren und danach mit dem regulären Würfel, um mit der Waffe zu treffen)");

                            //7
                        } else if (args[0].equalsIgnoreCase("Grauorks")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §9Raserei, §bFeiglinge, §9Kriegsmale, §bChaosgeflüster, §9Augenblitze, §bFinstereFesseln, §9ImmaterialeKopfnuss");

                        } else if (args[0].equalsIgnoreCase("Raserei")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender und dessen Verbündeten werden von einer wilden Raserei gepackt. Sie spüren keine Schmerzen und keine Verletzung kann sie bremsen und sie kennen keine Gnade. (ein physischer Treffer verursacht einmalig 2 Lp (bei Rüstung 2 Rp) Schaden.");

                        } else if (args[0].equalsIgnoreCase("Feiglinge")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie finstere Macht und das Gefühl von purer Überlegenheit, umgibt den Schamanen mit einer finsteren Aura, welche den Mut und die Moral von Nicht-Orks hemt und geistig schwache Wesen zur Flucht zwingt.");

                        } else if (args[0].equalsIgnoreCase("Kriegsmale")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane zeichnet schreckliche und blutrünstige Runen und Symbole auf sich oder einen Ork. Diese sorgen dafür, dass manche physische oder magische Angriffe verfehlen (gilt nur für einen Angriff/der betreffende Ork kann, bei einem verpatzen physischen Abwehrwurf, auch als Nicht-Magier den Magiewürfel (/würfel magie) benutzen, um dem Angriff dennoch zu entgehen. Bei magischen Angriffen, darf der betreffende Ork ebenfalls, mit dem Magiewürfel abwehren)");

                        } else if (args[0].equalsIgnoreCase("Chaosgeflüster")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane hört das Geflüster des Immateriums und gibt die Worte der unheilvollen Kräfte an seine Anhänger weiter. Wesen der Ordnung und der Reinheit fühlen sich unwohl bis verängstigt, während der Einfluss des Schamanen über die anderen Orks kurzfristig gesteigert wird und diese kurzfristig befehligen kann. (max.2min/der Häuptling ist vom 2ten Effekt ausgenommen/die beeinflussten Orks/die beeinflussten Orks, können nur auf den Häuptling gehetzt werden, wenn diese einen rplichen Groll gegen diesen hegen)");

                        } else if (args[0].equalsIgnoreCase("Augenblitze")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane schießt dunkel-grüne Blitze aus seinen Augen, welche dem Ziel leichte Verbrennungen bereitet und kurzzeitig lähmt. (Der Gegner setzt eine Runde aus oder verursacht 1 Lp Schaden/bei 1Lp wird der Gegner 2 min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("FinstereFesseln")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane beschwört bei Berührung des Zieles, dunkle Ketten die das Ziel fesseln. (Fessel hält, so lange der Schamane das Ziel berührt/ohne Berührung hält der Zauber max. 3min/im Kampf setzt der Gegner eine Runde aus)");

                        } else if (args[0].equalsIgnoreCase("ImmaterialeKopfnuss")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane lässt seinen, von den dunklen Mächten verdrehten Verstand, gegen den Verstand seines Zieles krachen. Dies sorgt beim Ziel temporär für Kopfschmerzen und verhindert die Fähigkeit sich zu konzentrieren. (gilt als Illusionszauber/blockiert magische Fähigkeiten für 1Runde/verursacht 1 Lp Schaden/außerhalb des Kampfes wird Magie 3min blockiert).");

                            //14
                        } else if (args[0].equalsIgnoreCase("Goblins")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §9JuckenderAusschlag, §bPesthauch §9Beulenpest, §bGiftigeBerührung, §9Störgeräusch, §bRostigeBerührung, §9ÄtzenderSchatten, §bKochendesPech, §9Mundfeule, §bUnkontrollierbarerLeibwind, §9SchleimigePusteln, §bÜblerGeruch, §9UnkontrollierbaresSchreien, §bVerdrehteWorte");

                        } else if (args[0].equalsIgnoreCase("JuckenderAusschlag")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §b Der Schamane lässt auf sein Ziel einen juckenden Ausschlag sprießen, welcher das Ziel dazu zwingt sich unkontrollierbar zu kratzen. (Gegner setzt eine Runde aus/bei 1 Lp wird der Gegner 1min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("Pesthauch")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Hauch des Schamanen verursacht eine Übelkeit, die in Erbrechen enden kann. (verursacht 1 Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("Beulenpest")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane lässt schmerzhafte Beulen auf sein Ziel wachsen, welche bei Berührung oder Schlägen große Schmerzen verursachen und es kurzzeitig lähmt. (Gegner setzt eine Runde aus + verursacht 1 Lp Schaden/bei 1 Lp wird der Gegner 1min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("GiftigeBerührung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane kann bei Berührung sein Ziel vergiften. Das Gift löst Wadenkrämpfe und Diarrhoe aus. Dies kann aber von jedem Medikus und Heilmagiekundigen problemlos geheilt werden. (Lähmung 4min/jeder Medikus, nur mit einer Heilpflanze (welches Pflanzenitem nutzt, ist der eigenen Kreativität vorbehalten) kann das betreffende Ziel vom Effekt heilen.)");

                        } else if (args[0].equalsIgnoreCase("Störgeräusch")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane lässt ein nerviges Geräusch in den Ohren seines Zieles ertönen, welches das Ziel ablenkt und kurzzeitig das Gehör raubt. (Der Gegner kann eine Runde keinen Angriff abwehren)");

                        } else if (args[0].equalsIgnoreCase("RostigeBerührung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane lässt die Waffen seiner Feinde verrosten. (Das Ziel muss im Rp-Kampf, 2mal treffen, damit ein LP abgezogen wird. Dies gilt, bis die Waffe gewechselt wird)");

                        } else if (args[0].equalsIgnoreCase("ÄtzenderSchatten")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane wirft eine Handvoll dunkler, zähflüssige Energie, welche zersetzende Eigenschaften aufweist. Diese sorgt bei ungeschützten Zielen für Schmerzen und Verätzung. (Wasser neutralisiert den zersetzenden Effekt/verursacht 1 Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("KochendesPech")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Schamane beschwört eine heiße, schwarze Substanz die beim Ziel für Verbrennungen sorgt. (verursacht 1 Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("Mundfeule")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §ber Goblin sorgt beim Ziel für starken, widerlichen Mundgeruch. (max. 3min)");

                        } else if (args[0].equalsIgnoreCase("UnkontrollierbarerLeibwind")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Goblin lässt das Ziel unkontrollierbare Flatulenzen entweichen. (1.min)");

                        } else if (args[0].equalsIgnoreCase("SchleimigePusteln")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Goblin lässt üble Pusteln auf den Körper des Zieles sprießen, welche beim Aufplatzen einen üblen Gestank von sich geben. Diese können von jedem Medikus, mühelos entfernt werden.");

                        } else if (args[0].equalsIgnoreCase("ÜblerGeruch")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Goblin sorgt beim Ziel für übelriechende Schweisausbrüche (Schweisausbruch 1.min /der Geruch hält den ganzen Tag an)");

                        } else if (args[0].equalsIgnoreCase("UnkontrollierbaresSchreien")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Goblin sorgt durch seine Magie, dass das Ziel unregelmäßig anfängt unkontrollierbar zu Schreien und/oder zu fluchen. (3min)");

                        } else if (args[0].equalsIgnoreCase("VerdrehteWorte")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Goblin verdreht die Aussprache des Zieles, weswegen das Ziel kaum noch klare Worte von sich geben kann und bei dem Versuch richtig zu reden nur Schwachsinn oder Kaudalwelsch von sich gibt.");

                            //23
                        } else if (args[0].equalsIgnoreCase("Dunkelelfen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §9Grausamkeitsfluch, §bVisuelleHexerei, §9Veritasfluch, §bArkaneUnterdrückung, §9Telepatie, §bStille, §9Schweigen, §bDämonenfratze, §9Trugbild, §bBlendwerk, §9Hypnose, §bFinsternis, §9Lebensabsorption, §bLebensübertragung, §9SanginiualeViskosität, §bSeregakontrolle, §9GeflüsterDesImmateriums, §bHöllenfeuer, §9Schattenschild, §bChaosblitze, §9FinstereEntladung, §bHandDesChaos, §9Spiegelschild");

                        } else if (args[0].equalsIgnoreCase("Grausamkeitsfluch")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender sorgt beim Ziel für grausame Schmerzen, dessen Ausmaße der Magier kontrollieren kann. (verursacht 1 Lp Schaden oder lässt den Gegner eine Runde aussetzen/gilt im Rp als Foltermöglichkeit)");

                        } else if (args[0].equalsIgnoreCase("VisuelleHexerei")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender macht das Ziel für eine gewisse Zeit blind. (2 min/im Kampf setzt der Gegner eine Runde aus)");

                        } else if (args[0].equalsIgnoreCase("Veritasfluch")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender belegt das Ziel mit einem Fluch, welcher für grausame Schmerzen sorgt, sollte das Ziel lügen. (5min dauer/verursacht bei einer Lüge verursacht 1 Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("ArkaneUnterdrückung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender unterdrückt die magischen Fähigkeiten des Zieles. (Blockiert für eine Runde, magischen Fähigkeiten des Zieles/im Rp werden die magischen Fähigkeiten blockiert, so lange das Ziel berührt wird)");

                        } else if (args[0].equalsIgnoreCase("Telephatie")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender kann eine telephatische Nachricht, an ein Ziel senden, mit welchem der Anwender Sichtkontakt hat. (eine Nachricht kostet eine tägliche Magieanwendung)");

                        } else if (args[0].equalsIgnoreCase("Stille")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §ber Anwender sorgt dafür, dass in einem Raum von 100m² kein Geräusch erklingen können.");

                        } else if (args[0].equalsIgnoreCase("Schweigen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender sorgt dafür, dass seine Worte nur von denen gehört werden, für den diese Worte bestimmt sind. (100m²/3min)");

                        } else if (args[0].equalsIgnoreCase("Dämonenfratze")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt optische Attribute von Dämonen an sich erscheinen, wie rot-glühende Augen, tiefe bedrohliche Stimme, Hörner, rote, brennende Haut oder Reißzähne. Die Grenzen liegen beim Anwender selbst. (max. 5min)");

                        } else if (args[0].equalsIgnoreCase("Trugbild")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört ein Trugbild, dessen Grenzen von der Kreativität des Anwenders abhängig sind. Der Zauber greift das Gehirn des Anwenders an, weswegen man nur ein Ziel anvisieren kann (max. 5 min)");

                        } else if (args[0].equalsIgnoreCase("Blendwerk")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt, sollte das Ziel angreifen, ein Objekt oder eine andere Person, als man selbst erscheinen, weswegen der Angriff auf das betreffende Objekt oder Person umgelenkt wird. (Das Ziel welches stattdessen angegriffen wird, kann regulär auf den Angriff reagieren)");

                        } else if (args[0].equalsIgnoreCase("Hypnose")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender sorgt dafür, dass das Ziel einschläft. Dieser Schlaf ist nicht magisch und gilt als normales Schlafen. (Im Kampf setzt der Gegner eine Runde aus)");

                        } else if (args[0].equalsIgnoreCase("Finsternis")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt einen Raum von 100m² in völliger Finsternis verdunkeln. Nur der Anwender kann sich dort noch orientieren.");

                        } else if (args[0].equalsIgnoreCase("Lebensabsorbation")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender ist in der Lage mit einer Phiole Blut, ein Teil der Lebensenergie des Zieles abzuzapfen, um Verletzungen oder Gebrechen zu heilen oder um das eigene Leben zu verlängern. (nimmt 1 Lp vom Ziel und heilt 1 Lp beim Anwender)");

                        } else if (args[0].equalsIgnoreCase("Lebensübertragung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender kann mit einem Tropfen seines eigenen Blutes, seine Lebenskraft auf ein anderes Lebewesen übertragen, um leichte Verletzungen oder einfache Krankheiten zu heilen, oder um das Leben des Zieles zu verlängern. (der Anwender nimmt sich 1 Lp um 1 Lp beim Ziel zu heilen)");

                        } else if (args[0].equalsIgnoreCase("SanginiualeViskosität")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt das Blut seines Zieles verdicken, um Blutungen zu stoppen. (heilt beim Ziel ein Lp, welches durch physische Angriffe verursacht wurde)");

                        } else if (args[0].equalsIgnoreCase("Seregakontrolle")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender ist in der Lage das Ziel alles sagen zu lassen, was man dem Ziel vorgibt, sowie kleinere Bewegungsaktionen durchzuführen (kein Kampf oder Magie). (5min Wirkdauer/das Blut ist nach 3 Anwendungen verbraucht)");

                        } else if (args[0].equalsIgnoreCase("GeflüsterDesImmateriums")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört schattenhafte Wesen, unterschiedlicher Größe und Gestalt, welche in einer vergessenen, unheiligen Sprache verbotene Wahrheiten und grausame Lügen flüstern, welche mehr Wahrheit beinhalten, als die Wahrheit selbst.");

                        } else if (args[0].equalsIgnoreCase("Höllenfeuer")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender wirft eine handvoll schwarzen Feuers auf sein Ziel. Das Feuer verursacht leichte, aber schwer heilbare Verbrennungen. (verursacht 1 Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("Schattenschild")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört einen Schild aus finster Energie welcher ein Zauber, wie ein schwarzes Loch absorbiert.");

                        } else if (args[0].equalsIgnoreCase("Chaosblitze")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt schwarze oder rote Blitze aus seinen Händen fahren, die Verbrennungen verursachen und das Ziel lähmen (1 Lp Schaden oder der Gegner setzt eine Runde aus/bei 1 Lp wird der Gegner 3min gelähmt)");

                        } else if (args[0].equalsIgnoreCase("FinstereEntladung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender entlädt eine geballte Ladung Chaosenergie, welche das Ziel von den Füßen reißt. (stößt das Ziel 3 m zurück/verursacht 1 Lp Schaden)");

                        } else if (args[0].equalsIgnoreCase("HandDesChaos")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Hand des Anwenders fängt an sich mit schwarzer Energie zu umhüllen. Auf Wesen der Reinheit und der Ordnung wirkt eine Berührung sehr schmerzhaft. (verursacht 1 Lp Schaden bei Wesen der Ordnung)");

                        } else if (args[0].equalsIgnoreCase("Spiegelschild")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört ein magischen Spiegel, der ein feindliche Zauber reflektiert. (die Zauber werden bei Erfolg abgewehrt. Bei einem Würfelwert von 10 wird der Zauber auf den Feind reflektiert)");

                            //11
                        } else if (args[0].equalsIgnoreCase("Hochzwerge")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §9Astralschniede, §bGranitverzauberung, §9AufloderndeBeschwörung, §bIrdischesInferno, §9Wasserströmung, §bSturmschneide, §9ArkaneAbwehr, §bMemetischeVerteidigung, §9SchildDerReinheit, §bGeweihteAura, §9Hexenfessel");

                        } else if (args[0].equalsIgnoreCase("Astralschneide")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Waffe wird mit, einem Feld aus reiner magischer Energie über die Schneide gezogen, wodurch die Waffe mehr Schärfe bekommt (beim Treffer werden bei ungerüsteten Gegner 2 Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("Granitverzauberung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Waffe wird mit einer harten steinernden Aura überzogen, die der Waffe eine gewaltige Wucht verleiht. (Ignoriert den Rp bei Lederrüstung/bei ungerüsteten Gegner werden 2 Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("AufloderndeBeschwörung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Waffe entfacht eine Flamme, die den Gegner verbrennt.(Ignoriert den Rp bei Ketten und Goldrüstung/bei ungerüsteten Gegnern werden zwei Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("IrdischesInferno")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Waffe wird mit Magma umschlossen, welches Eisen schmelzen kann.(Ignoriert den Rp bei Eisenrüstung/bei ungerüsteten Gegner werde zwei Lp abgezogen Verbrennung)");

                        } else if (args[0].equalsIgnoreCase("Luggy")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDas Plugin wurde von LuggyLuk gemacht");

                        } else if (args[0].equalsIgnoreCase("LuggyLuk")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDas Plugin wurde von LuggyLuk gemacht");

                        } else if (args[0].equalsIgnoreCase("Luggy_Luk")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDas Plugin wurde von LuggyLuk gemacht");

                        } else if (args[0].equalsIgnoreCase("Wasserströmung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Waffe entlädt ein unglaublich starken Wasserstrom, welcher die stärkste Rüstung durchdringt. (Ignoriert den Rp bei Diamantrüstung/bei ungerüsteten Gegnerwerden 2 Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("Sturmschneide")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Waffe schneidet mit einer hauchdünnen Luftklinge, welche das Ziel auf der kleinster Ebene zerschneidet. (Ignoriert den Rp bei Netheritrüstungen/bei ungerüsteten Gegner werden 2 Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("ArkaneAbwehr")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender zeichnet magische Runen der Abwehr in die Luft, die den Anwender oder ein anderes Ziel vor magischen Angriffen schützen.(Der Beschwörer wehrt bei Erfolg, einen magischen Angriff ab. Wirkt nicht gegen Chaosmagie oder Magie, die die Psyche angreift)");

                        } else if (args[0].equalsIgnoreCase("MemetischeVerteidigung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender verschließt sein Geist mit seiner Magie, wodurch dieser nicht von psychischen Angriffen getroffen werden kann (der Träger wehrt bei Erfolg eine mental-beeinflussbare Fähigkeiten/Ilusionszauber ab)");

                        } else if (args[0].equalsIgnoreCase("SchildDerReinheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender umgibt sich mit reiner, weißer Magie, die die verdorbene Magie des Chaos vertreiben kann. Entweder bei sich selbst oder bei einem anderen Ziel. (Der Anwender darf bei Erfolg, einen Chaoszauber abwehren.)");

                        } else if (args[0].equalsIgnoreCase("GeweihteAura")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender erschafft eine Aura aus weißer Magie, die für Chaoswesen nicht zu ertragen ist. (Der Anwender kann nicht von einem Chaoswesen berührt werden. Ein Chaoswesen einen Abstand von 1m zum Träger halten/Fläche 9m²/max 2min)");

                        } else if (args[0].equalsIgnoreCase("Hexenfessel")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender blockiert die magischen Energien eines Zieles, wodurch dieses keine Magie wirken kann. (Blockiert eine Runde, magische Fähigkeiten/außerhalb eines Kampfes 3min Magieblockade)");

                            //14
                        } else if (args[0].equalsIgnoreCase("Eisenzwerge")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §9FinstereBessenheit, §bVerhornteBessenheit, §9InfernaleBessenheit, §bSchmelzendeBessenheit, §9ZersetzendeBessenheit §bKorumpierendeBessenheit, §9DämonischerSchild, §bRekorrumpierendeBesitznahme, §9UnheiligeBessenheit, §bEntweihteBessenheit, §9StählerndeTransmutation, §bChaossiegel, §9MagischeAntizipation, §bOkkutleVortex");

                        } else if (args[0].equalsIgnoreCase("FinsterBessenheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Dämon erzeugt ein Feld aus reiner Chaosenergie über die Schneide und sorgt dafür für mehr Schärfe.(beim Treffer werden bei ungerüsteten Gegner 2 Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("VerhornteBessenheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Dämon bildet um die Dämonenwaffe eine extrem harte Hornschicht, welche der Waffe eine gewaltige Wucht verleiht. (Ignoriert den Rp bei Lederrüstung/bei ungerüsteten Gegner werden 2 Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("InfernaleBessenheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Zorn des gefangenen Dämons entfacht eine höllische Flamme, die den Gegner verbrennt. (Ignoriert den Rp bei Kettenrüstung/bei ungerüsteten Gegnern werden 2 Lp abgezogen + Verbrennung)");

                        } else if (args[0].equalsIgnoreCase("SchmelzendeBessenheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Hass des Dämons in der Waffe, lässt selbst Eisen schmelzen. (ignoriert den Rp bei Eisenrüstung/bei ungerüsteten Gegnern werden 2 Lp abgezogen + Verbrennung)");

                        } else if (args[0].equalsIgnoreCase("ZersetzendeBessenheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Dämon in der Waffe sondert eine zersetzende Energie ab, welche die stärkste Rüstung durchdringt. (Ignoriert den Rp bei Diamantrüstung/bei ungerüsteten Gegner werden 2 Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("KorrumpierendeBessenheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer mächtige Dämon in der Waffe, lässt die Essenz des Zieles korrumpieren und sich den dunklen Mächten und dessen zerstörerischen Willen beugen. (Ignoriert den Rp bei Netheritrüstungen/bei ungerüsteten Gegner werden 2 Lp abgezogen)");

                        } else if (args[0].equalsIgnoreCase("DämonischerSchild")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört einen dämonischen Schild, der einen Chaoszauber abwehrt.(Der Anwender hat die Chanche einen Schutzwurf zu würfen, um einen Chaoszauber abzuwehren.)");

                        } else if (args[0].equalsIgnoreCase("RekorrumpierendeBesitznahme")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört einen Dämon, der sich in dessen Gehirn festsetzt und dieses vor mentalen Angriffen schützt. (der Anwender darf eine mental-beeinflussbarn Zauber/Illusionszauber abwehren)");

                        } else if (args[0].equalsIgnoreCase("UnheiligeBessenheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört einen mächtigen Dämon, der selbst weiß-magische oder arkane Magie absorbieren kann. (Der Anwender wehrt einen arkanen oder weiß-magischen Zauber, bei Erfolg ab)");

                        } else if (args[0].equalsIgnoreCase("EntweihteBessenheit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört einen Dämon, der diesen mit einer unheiligen Aura umhüllt, die schädlich und vertreibend auf Wesen der Ordnung und der Reinheit wirken. (Der Anwender kann nicht von einem Wesen der Ordnung und des Lichts berührt werden. Auch muss ein Wesen des Lichts einen Abstand von 1m zum Träger halten./Fläche 9m²/Dauer: 2min)");

                        } else if (args[0].equalsIgnoreCase("StählerndeTransmutation")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt seine Haut instabil mutieren, wo durch diese, kurzzeitig so hart und biegsam wie Stahl wird. (Gibt dem Anwender 1 Rp für 5min. Der Rp ist durch Heilmagie heilbar.)");

                        } else if (args[0].equalsIgnoreCase("Chaossiegel")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört bei Berührung ein Chaossiegel auf das Ziel, die magische Fähigkeiten unterdrückt und das Ziel Bewegungsunfähig macht (Gegner setzt 1 Runde aus/Gegner mit 1 Lp sind 4min gelähmt/Magie wird außerhalb von Kämpfen 3min unterdrückt)");

                        } else if (args[0].equalsIgnoreCase("MagischeAntizipation")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender verstärkt kurzzeitig seine kognitiven Fähigkeiten und kann die Angriffe seine Gegner deutlich besser antizipieren. Dadurch kann der Anwender einen feindlichen Angriff entgehen. (Bei Erfolg geht ein feindlicher Angriff daneben)");

                        } else if (args[0].equalsIgnoreCase("OkkutleVortex")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender kann eine Vortex aus chaotischem Nebel erschaffen welche nahe Objekte anzieht wobei der Anwender ausgeschlossen ist. Wirkung 1: Zieht Spieler zwischen 2m bis 10m Entfernung zu sich ran (1m - 5m nähe). Wirkung 2: Zieht Spieler zwischen 2m bis 10m Entfernung zu sich ran (bis auf 1m) und lässt sie ihr Gleichgewicht verlieren. (Gegner setzt eine Runde aus)./kostet 2 Magieanwendungen)");

                            //21
                        } else if (args[0].equalsIgnoreCase("Lichtelfen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §9HeilenderSegen, §bWirbelDerZeit, §9Lichtstrahlen, §bBeruhigungsschlaf, §9Auszehrung, §bLichtspiegel, §9Sternennetz, §bHauchDerGezeiten, §9SchildDerGezeiten, §bZeitlicherSegen, §9PranamayasHilfe, §bPranamayasHilfe, §9Lichthülle, §bLichtrüstung, §9Ewigkeitsschild, §bSternenlicht, §9Mondstaub, §bSonnenstrahlen, §9ZornDerGötter, §bLichtwaffe, §9KnallDerZeit, §bSpeereDesUniversums");

                        } else if (args[0].equalsIgnoreCase("HeilenderSegen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bAn den Händen des Anwenders beginnen die Runen, in einem hellen Grün zu Leuchten. Sie verschließen kleinere Wunden und entspannen Schmerzen (Heilt 1 Lp)");

                        } else if (args[0].equalsIgnoreCase("WirbelDerZeit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört eine helle Regenbogenfarben leuchtende Rune auf dem Ziel. An der Stelle wo die Rune gewirkt wird, beginnt die Zeit sich um ein paar Minuten zurückzudrehen, sodass kürzlich zugefügte Verletzungen verschwinden (Vordert 1 Lp des Anwenders und schenkt dem Ziel 1 Lp)");

                        } else if (args[0].equalsIgnoreCase("Lichtstrahlen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bIn den Händen der Elfe erscheint ein Runenkreis, welcher Lebewesen heilen kann. Ihnen wird Kraft und Ruhe gespendet. (Heilt 1 Lp)");

                        } else if (args[0].equalsIgnoreCase("Beruhigungsschlaf")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört ein Runensiegel um sein Ziel. Jene Aktion erfordert, je nach dem ob das Ziel sich wehren kann oder nicht, mehr oder weniger Kraft. Das Ziel wird, sobald der Anwender es berührt ihn einen Schlaf versetzt welcher einige Stunden dauern kann. Die Voraussetzung, ist das das Ziel starke Panik haben muss, wie auch kaum einen klaren Gedanken fassen kann.");

                        } else if (args[0].equalsIgnoreCase("Auszehrung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört eine nahezu winzige Rune in seinen Händen und kann diese an die Stirn seines Ziels halten. Sollte Gift in dem Ziel sein so wird der Anwender dieses in die Rune ziehen können. (Heilt Vergiftungen)");

                        } else if (args[0].equalsIgnoreCase("Lichtspiegel")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört über sich einen großen Runenspiegel. Unterschiedlich wieviele Wesen in der Nähe sind, fällt die Heilung aus. Bei 4 Wesen wird das Ziel um 2 Lp geheilt. Unter 4 Wesen um 1 Lp.");

                        } else if (args[0].equalsIgnoreCase("Sternennetz")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört verschiedene kleine Runen welche sich auf eine große Wunde fixieren. Sie spinnen eine Art Netz an der Wunde und schützen so vor weiterem Schaden. Nach 30 Minuten kann eine Handgroße Wunde verschlossen sein.");

                        } else if (args[0].equalsIgnoreCase("HauchderGezeiten")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bAn den Händen des Anwenders entstehen vier kleine weiße Kreise, welche auf Befehl zu einer Person fliegen und ihre Ausdauer erhöhen und sie schneller machen.");

                        } else if (args[0].equalsIgnoreCase("SchildDerGezeiten")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bEin Tellerbreites Runensiegel bildet sich an den Armen des Anwenders. Es kann kleinere Angriffe abwehren und fungiert wie ein normales Schild");

                        } else if (args[0].equalsIgnoreCase("ZeitlicherSegen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDie Lichtelfe beginnt sich selbst in einer gigantischen Runenblase, welche golden Leuchtet einzuhüllen. In jenem Moment geht sie einen Tausch mit Anwa ein. Ihr Lebensgeist wird dem ihren Körper entzogen und ein verstorbener ihrer Wahl, darf zurückkehren.\n" +
                                               "\n" +
                                               "Der Zauber ist fähig das Leben einer Elfe gegen das eines bis zu zwei Tage verstorbenen einzutauschen. Jedoch ist es nicht sicher das der Anwender den Lebenden zurückholt. Bei einem nicht erfolgreichen Magie Wurf, wird ein normaler Würfel geworfen. Wenn dieser Rot ausfällt, wird dem Anwender jede Magie entzogen. Er wird zu einer Versuchten Lichtelfe, magieunfähig. Sollte der Würfel grün fallen, so scheitert der Zauber und der Elf wird für einen Monat unfähig sein Magie zu wirken.\n" +
                                               "\n" +
                                               "Sollte der Zauber gelingen und die Person wiederbelebt werden so kann man bemerken das die Person sehr schwach ist. Sie wird in der ersten Zeit nichtmal fähig sein sich eigenständig zu bewegen oder gar wirklich zu agieren.");

                        } else if (args[0].equalsIgnoreCase("PranamayasHilfe")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender kann eine Fenstergroße Rune beschwören, die Steinschläge oder Angriffe abwehren kann. Man ist fähig mehrere zu schützen. Durch die Größe wird dem Anwender viel Kraft entzogen.");

                        } else if (args[0].equalsIgnoreCase("Lichthülle")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beginnt einen Runenkreis um sich zu zeichnen, dabei benötigt er die Perle welche man bei seiner Volljährigkeit erhält. So kann sich die Person oder das Ziel in bloßes Licht hüllen, sodass es für einige Zeit unangreifbar wird. (6 Minuten)");

                        } else if (args[0].equalsIgnoreCase("Lichtrüstung")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender hüllt seinen Körper in ein helles Licht welches ihn gut schützt. Die Lichtrüstung bleibt so lange, bis der Anwender Bewusstlos wird oder sein Runa aufgebracht ist (Keine Magie für den Tag mehr)");

                        } else if (args[0].equalsIgnoreCase("Ewigkeitsschild")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender kann ein permanentes Schild auf sich oder auf ein Ziel legen. Dieses Schild ist so lange aktiv, wie der Anwender noch bei Bewusstsein ist. Es schützt vor dem ersten Angriff und Feuer oder anderen Wunden");

                        } else if (args[0].equalsIgnoreCase("Sternenlicht")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt einen helles violett/blauen Runenkreis erstrahlen welches den Gegner blendet und konzentrationsunfähig macht");

                        } else if (args[0].equalsIgnoreCase("Mondstaub")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bHinter dem Anwender entsteht ein großer grauer Runenkreis aus welchem grauer Staub beginnt zu fallen. Bei Berührung der Haut, lässt er das Opfer bewusstlos werden (5 Minuten)");

                        } else if (args[0].equalsIgnoreCase("Sonnenstrahlen")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender lässt über seinem Kopf drei Runensiegel entstehen, aus welchen Lichtstrahlen schießen (1 Lp schaden)");

                        } else if (args[0].equalsIgnoreCase("ZornDerGötter")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört einen großen Runenkreis über einer Person. Aus diesem wird ein Lichtblitz gesendet, welcher auf das Opfer trifft. (1 Lp schaden)");

                        } else if (args[0].equalsIgnoreCase("Lichtwaffe")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender ist fähig eine Waffe aus reinem Licht in seinen Händen zu formen. Diese leuchtet Hell und vorallem sehr weit. (Level Diamantwaffe)");

                        } else if (args[0].equalsIgnoreCase("KnallDerZeit")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört eine Rune welche einen gigantisch lauten Knall erzeugt. Alle im Umkreis können, sollten sie sich nicht die Ohren zuhalten, mit schweren Schäden davon kommen. (1 Lp schaden)");

                        } else if (args[0].equalsIgnoreCase("SpeereDesUniversums")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §bDer Anwender beschwört einen kleinen Runenkreis vor sich aus welchem er kleine Gesteinsähnliche spitze Brocken schießen kann. Die Anzahl ist unbegrenzt. Der Zauber löst sich nach 2 Minuten auf (1 Lp schaden)");

                        } else if (args[0].equalsIgnoreCase("Forum")) {
                            player.sendMessage("§7[§5FTS-Engine§7] §b Hier hast du den Link zu der Magie-Seite des Forums: §8 https://forum.ftscraft.de/t/tutorial-so-funktioniert-magie-auf-unserem-server/8363");

                        } else {
                            player.sendMessage("§7[§5FTS-Engine§7] Bitte benutze den Command so: §c/zauber §8[§cZaubername§8]§7.");
                        }
                    } else {
                        sender.sendMessage("§7[§5FTS-Engine§7] §6Du musst ein §cSpieler §6sein!");
                    }

                }
            } else
                player.sendMessage("§7[§5FTS-Engine§7] §6Du hast §ckeine Rechte§6 um diesen Befehl auszuführen");


        }
        return false;
    }
}
