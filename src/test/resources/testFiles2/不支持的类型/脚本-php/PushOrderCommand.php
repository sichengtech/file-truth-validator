<?php

namespace App\Command;

use Hyperf\Command\Command as HyperfCommand;
use App\Service\Distance\PushOrderQueueService;
use Hyperf\Command\Annotation\Command;
use Psr\Container\ContainerInterface;
use Psr\EventDispatcher\EventDispatcherInterface;

/**
 * @Command
 */
class PushOrderCommand extends HyperfCommand
{
    /**
     * @var ContainerInterface
     */
    protected $container;
    private $pushOrderQueueName = 'erp-oms';

    protected $name = 'queue:erp-oms';

    public function __construct(ContainerInterface $container, EventDispatcherInterface $eventDispatcher)
    {
        $this->container = $container;
        $this->eventDispatcher = $eventDispatcher;

        parent::__construct();
    }

    public function configure()
    {
        parent::configure();
        $this->setDescription('推送预售订单至开放平台');
    }

    public function handle()
    {
        $queueCustomer = $this->container->get(PushOrderQueueService::class);
        if (!empty($queueCustomer)) {
            $queueCustomer->begin($this->pushOrderQueueName);
        }
    }
}