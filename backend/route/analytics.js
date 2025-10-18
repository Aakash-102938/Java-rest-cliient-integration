const express = require('express');
const auth = require('../middleware/auth');
const Task = require('../models/Task');
const Activity = require('../models/Activity');

const router = express.Router();

// Get activity data for heatmap
router.get('/activity', auth, async (req, res) => {
  try {
    const oneYearAgo = new Date();
    oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);

    const activities = await Activity.find({
      user: req.userId,
      date: { $gte: oneYearAgo }
    });

    const activityData = {};
    activities.forEach(activity => {
      const dateStr = activity.date.toISOString().split('T')[0];
      activityData[dateStr] = activity.count;
    });

    res.json(activityData);
  } catch (error) {
    res.status(500).json({ message: 'Server error', error: error.message });
  }
});

// Get dashboard stats
router.get('/stats', auth, async (req, res) => {
  try {
    const totalTasks = await Task.countDocuments({ user: req.userId });
    const completedTasks = await Task.countDocuments({ 
      user: req.userId, 
      completed: true 
    });
    const pendingTasks = totalTasks - completedTasks;

    res.json({
      totalTasks,
      completedTasks,
      pendingTasks,
      projects: 12 // This could be dynamic if you add project model
    });
  } catch (error) {
    res.status(500).json({ message: 'Server error', error: error.message });
  }
});

module.exports = router;