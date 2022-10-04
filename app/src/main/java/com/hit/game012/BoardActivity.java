package com.hit.game012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gameplay.BoardView;
import com.hit.game012.gameplay.GamePlay;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    private class ViewPagerAdapter extends FragmentStatePagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
    private ViewPager mViewPager;
    private GamePlay gamePlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        int boardSize = (int) getIntent().getExtras().get("size");
        mViewPager = findViewById(R.id.board_fragment);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        gamePlay = new GamePlay();
        gamePlay.startGame(boardSize);
        adapter.addFragment(new BoardView(gamePlay.getBoard()), "Board");
        mViewPager.setAdapter(adapter);


    }
}