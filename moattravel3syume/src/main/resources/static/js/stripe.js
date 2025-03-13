const stripe = Stripe('pk_test_51QwHjVH6R7DxdL8DOfoPGKgYU5jYGPzvMyYvK3ZZoShFY2XvTENscPreLTaewq0z682z6djE5MJzAsAf0FwBSmLX00l3qLwyRy');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
  stripe.redirectToCheckout({
    sessionId: sessionId
  })
});
