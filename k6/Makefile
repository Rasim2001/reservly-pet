BASE_URL ?= http://localhost:8080

.PHONY: rate-limit
rate-limit:
	@echo "Rate limit test on /api/auth/login..."
	k6 run -e BASE_URL=$(BASE_URL) rate-limit.js