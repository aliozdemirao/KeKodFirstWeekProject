package com.aliozdemir.kekodfirstweekproject.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aliozdemir.kekodfirstweekproject.MainActivity
import com.aliozdemir.kekodfirstweekproject.R
import com.aliozdemir.kekodfirstweekproject.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var switches: List<SwitchMaterial>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = (requireActivity() as MainActivity).getBottomNavigationView()
        switches = getSwitches()

        initializeSwitchStates()
        setupObservers()
        setupSwitchListeners()
    }

    private fun initializeSwitchStates() {
        binding.switchEgo.isChecked = viewModel.getInitialEgoState()
        updateSwitchesAccessibility()
    }

    private fun setupObservers() {
        viewModel.isEgoChecked.observe(viewLifecycleOwner) { isChecked ->
            binding.switchEgo.isChecked = isChecked
            updateSwitchStates(isChecked)
            updateSwitchesAccessibility()
        }

        viewModel.bottomNavVisibility.observe(viewLifecycleOwner) { visibility ->
            bottomNavigationView.visibility = visibility
        }
    }

    private fun setupSwitchListeners() {
        binding.switchEgo.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onEgoSwitchChanged(isChecked)
            updateSwitchStates(isChecked)
            updateSwitchesAccessibility()
        }

        switches.forEach { switch ->
            switch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.handleOtherSwitchChange(isChecked)
                handleSwitchMenuItem(bottomNavigationView.menu, switch)
                updateSwitchesAccessibility()
            }
        }
    }

    private fun updateSwitchStates(isEgoChecked: Boolean) {
        switches.forEach { switch ->
            switch.isEnabled = !isEgoChecked
            if (isEgoChecked) switch.isChecked = false
        }
    }

    private fun getSwitches() =
        listOf(
            binding.switchHappiness,
            binding.switchOptimism,
            binding.switchKindness,
            binding.switchGiving,
            binding.switchRespect,
        )

    private fun handleSwitchMenuItem(
        menu: Menu,
        switch: SwitchMaterial,
    ) {
        val (itemId, titleResId, iconResId) =
            when (switch.id) {
                R.id.switchHappiness ->
                    Triple(
                        R.id.happinessFragment,
                        R.string.title_happiness,
                        R.drawable.ic_happiness,
                    )

                R.id.switchOptimism ->
                    Triple(
                        R.id.optimismFragment,
                        R.string.title_optimism,
                        R.drawable.ic_optimism,
                    )

                R.id.switchKindness ->
                    Triple(
                        R.id.kindnessFragment,
                        R.string.title_kindness,
                        R.drawable.ic_kindness,
                    )

                R.id.switchGiving ->
                    Triple(
                        R.id.givingFragment,
                        R.string.title_giving,
                        R.drawable.ic_giving,
                    )

                R.id.switchRespect ->
                    Triple(
                        R.id.respectFragment,
                        R.string.title_respect,
                        R.drawable.ic_respect,
                    )

                else -> return
            }

        if (switch.isChecked) {
            addMenuItem(menu, itemId, titleResId, iconResId)
        } else {
            removeMenuItem(menu, itemId)
        }
    }

    private fun addMenuItem(
        menu: Menu,
        itemId: Int,
        titleResId: Int,
        iconResId: Int,
    ) {
        if (menu.findItem(itemId) == null) {
            val menuItem = menu.add(0, itemId, menu.size(), getString(titleResId))
            menuItem.setIcon(iconResId)
        }
    }

    private fun removeMenuItem(
        menu: Menu,
        itemId: Int,
    ) {
        menu.removeItem(itemId)
    }

    private fun updateSwitchesAccessibility() {
        val maxMenuItems = 5
        val menuItemCount = bottomNavigationView.menu.size()

        switches.forEach { switch ->
            switch.isEnabled = if (menuItemCount >= maxMenuItems) switch.isChecked else true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
