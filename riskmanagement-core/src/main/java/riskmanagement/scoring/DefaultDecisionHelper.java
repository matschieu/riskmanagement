package riskmanagement.scoring;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Matschieu
 *
 */
public class DefaultDecisionHelper implements DecisionHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDecisionHelper.class);

	private double minThreshold;

	private double maxThreshold;

	/**
	 *
	 * @param minThreshold: minThreshold should be lower than or equals to maxThreshold
	 * @param maxThreshold: maxThreshold should be greater than minThreshold
	 * @throws IllegalArgumentException
	 */
	public DefaultDecisionHelper(double minThreshold, double maxThreshold) throws IllegalArgumentException {
		if (minThreshold > maxThreshold) {
			throw new IllegalArgumentException("The max threshold should be greater than min threshold");
		}
		this.minThreshold = minThreshold;
		this.maxThreshold = maxThreshold;
	}

	@Override
	public double getMinThreshold() {
		return minThreshold;
	}

	@Override
	public double getMaxThreshold() {
		return maxThreshold;
	}

	@Override
	public double calculateFinalScore(List<RuleResult> ruleResults) {
		double score = 0;

		if (ruleResults != null && !ruleResults.isEmpty()) {
			int nbApplicableRules = 0;

			for(RuleResult ruleResult : ruleResults) {
				if (ruleResult == null) {
					continue;
				}
				if (ruleResult.isApplicable()) {
					score += ruleResult.getScore();
					nbApplicableRules++;
				}
			}

			if (nbApplicableRules != 0) {
				score = score / nbApplicableRules;
			}
		}

		LOGGER.info("Score = {}", score);

		return score;
	}

	@Override
	public Color calculateColor(double score) {
		Color color = null;

		if (score > maxThreshold) {
			color = Color.GREEN;
		} else if (score >= minThreshold) {
			color = Color.ORANGE;
		} else {
			color = Color.RED;
		}

		LOGGER.info("Color = {}", color);

		return color;
	}

}
