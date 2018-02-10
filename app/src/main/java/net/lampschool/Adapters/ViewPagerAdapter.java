package net.lampschool.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.lampschool.Fragments.FragmentAssenze;
import net.lampschool.Fragments.FragmentComunicazioni;
import net.lampschool.Fragments.FragmentLezioni;
import net.lampschool.Fragments.FragmentMedie;
import net.lampschool.Fragments.FragmentNote;
import net.lampschool.Fragments.FragmentRitardi;
import net.lampschool.Fragments.FragmentUscite;
import net.lampschool.Fragments.FragmentValutazioni;
import net.lampschool.Utils.Account;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titoli;
    private int numTabs;
    private String dati;
    private Account account;


    public ViewPagerAdapter(FragmentManager fm, String[] titoli, int numTabs, String dati, Account account) {
        super(fm);
        this.titoli = titoli;
        this.numTabs = numTabs;
        this.dati = dati;
        this.account = account;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentValutazioni().newInstance(dati, account);
            case 1:
                return new FragmentMedie().newInstance(dati, account);
            case 2:
                return new FragmentAssenze().newInstance(dati, account);
            case 3:
                return new FragmentRitardi().newInstance(dati, account);
            case 4:
                return new FragmentUscite().newInstance(dati, account);
            case 5:
                return new FragmentNote().newInstance(dati, account);
            case 6:
                return new FragmentComunicazioni().newInstance(dati, account);
            case 7:
                return new FragmentLezioni().newInstance(dati, account);
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titoli[position];
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
